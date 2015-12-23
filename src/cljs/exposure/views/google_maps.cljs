(ns exposure.views.google-maps
  (:require [reagent.core :as r]
            [taoensso.timbre :as log :include-macros :true]
            [goog.dom :as dom]))

(defn create-latlng [{:keys [lat lng]}]
  (js/google.maps.LatLng. lat lng))


;; We store references to Google Maps objects
;; and DOM nodes they are rendered to because there is
;; memory leak in API, which causes serious damage with React
;; See more: https://code.google.com/p/gmaps-api-issues/issues/detail?id=3803
(defonce instances (atom {}))

(defn create-cached-gmaps
  "Creates gmaps objects and stores them in cache"
  [id]
  (let [dom (.createElement js/document "div")
        _   (.setAttribute dom "id" (str (name id) "-cached"))
        obj (js/google.maps.Map. dom (clj->js {"zoom" 9}))
        result [dom obj]]
    (swap! instances #(assoc % id result))
    [dom obj]))

(defn get-cached-gmaps
  "Loads objects from cache or creates them and stores in cache then)"
  [id]
  (or
    (@instances id)
    (create-cached-gmaps id)))

(defn gmap
  "Creates map component with reusable instance of Google Maps DOM node
   To ensure clean reusability, please provide unique :id in props"
  [props]
  (let [[gmaps-dom gmaps-obj] (get-cached-gmaps (:id props "global"))
        update-fn  (fn [component]
                     (log/debug "Updating, component props:" (r/props component))
                     (let [{center :center} (r/props component)
                           latlng (create-latlng center)]
                       (.panTo gmaps-obj latlng)))]

    (r/create-class {:display-name "gmaps-inner-stateful"

                     :reagent-render
                     (fn [{style :style id :id}]
                       [:div {:style style :id id}])

                     :component-did-mount
                     (fn [this]
                       (log/debug :component-did-mount)
                       (dom/appendChild (r/dom-node this) gmaps-dom)
                       (set! (-> gmaps-dom .-style .-width)  "100%")
                       (set! (-> gmaps-dom .-style .-height) "100%")
                       (js/google.maps.event.trigger gmaps-obj "resize")
                       (update-fn this))

                     :component-will-unmount
                     (fn [this]
                       (log/debug :component-will-unmount)
                       (let [canvas (r/dom-node this)]
                         (dom/removeChildren canvas)))

                     :component-will-receive-props
                     (fn [this new-argv]
                       (log/debug "Current props" (r/props this))
                       (log/debug "Next props" new-argv))

                     :component-did-update
                     (fn [this]
                       (log/debug :component-did-update)
                       (update-fn this))})))