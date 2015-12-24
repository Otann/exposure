(ns exposure.views.mapbox
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [taoensso.timbre :as log :include-macros true]
            [exposure.config :as config]))

(defonce init
  (aset js/mapboxgl "accessToken" config/mapbox-token))

(defn- feature [coordinates]
  {:type "Feature"
   :geometry {:type "Point"
              :coordinates coordinates}})

(defn- geojson [points]
  {:type "FeatureCollection"
   :features (vec (map feature points))})

(defn- find-bounds [points]
  (log/debug "Founding bounds for points" points)
  (loop [[[lng lat] & xs] points
         bounds (js/mapboxgl.LngLatBounds. (js/mapboxgl.LngLat. lng lat)
                                           (js/mapboxgl.LngLat. lng lat))]
    (if (empty? xs)
      (do
        (log/debug "Found bounds")
        (js/console.log bounds)
        bounds)
      (recur xs (.extend bounds (js/mapboxgl.LngLat. lng lat))))))

(defn mapbox-stateful
  [props]
  (let [mapbox-js   (atom nil)
        source-name "posts"
        source      (js/mapboxgl.GeoJSONSource. {:data (geojson (:points props))})
        update-fn (fn [this]
                    (let [props  (r/props this)
                          points (:points props)
                          total  (count points)]

                      ; updating source for points
                      (.setData source (clj->js (geojson points)))

                      ; fit all points to map
                      (cond
                        (= total 1) (doto @mapbox-js
                                      (.setCenter (clj->js (first points)))
                                      (.setZoom 12))
                        (> total 1) (.fitBounds @mapbox-js (find-bounds points) (js/Object.)))))]
    (r/create-class
      {:display-name "MapboxMapGL"

       :reagent-render
       (fn [props] [:div#mapbox {:style (:style props)}])

       :component-did-mount
       (fn [this]
         (log/debug :component-did-mount)
         (let [{center :center
                zoom :zoom} (r/props this)
               map-config {:container (r/dom-node this)
                           :center [(:lng center) (:lat center)]
                           :zoom zoom
                           :style "mapbox://styles/mapbox/streets-v8"}
               map (js/mapboxgl.Map. (clj->js map-config))]

           (reset! mapbox-js map)
           (.on map "style.load"
                (fn []
                  (.addSource map source-name source)
                  (.addLayer map (clj->js {:id "posts"
                                           :type "circle"
                                           :source source-name}))))
           (update-fn this)))

       :component-did-update
       (fn [this]
         (log/debug :component-did-update)
         (update-fn this))

       :component-will-receive-props
       (fn [this [_ new-props]]
         (log/debug "Current props" (r/props this))
         (log/debug "Next props" new-props))

       :component-will-unmount
       (fn [this]
         (log/debug :component-will-unmount))})))