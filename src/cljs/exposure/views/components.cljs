(ns exposure.views.components
  (:require [reagent.ratom :as r :refer-macros [reaction]]
            [re-frame.core :refer [dispatch subscribe]]
            [exposure.routes :refer [url-for]]
            [taoensso.timbre :as log :include-macros true]
            [exposure.views.mapbox :refer [mapbox-stateful]]))


(defn search-form []
  (let [data (subscribe [:search-input])]
    (fn [token]
      [:form.ui.form
       {:on-submit (fn [e] (doto e
                             (.preventDefault)
                             (.stopPropagation))
                           (dispatch [:search-init @data token]))}
       [:div.ui.action.input.fluid
        [:input {:type "text"
                 :value @data
                 :on-change #(dispatch [:search-input (-> % .-target .-value)])}]
        [:button.ui.button "Search"]]])))

(defn post [data]
  (let [img (-> data :images :thumbnail)
        size "6em"]
    ^{:key (:id data)}
    [:div.six.doubling.card
     {:style {:width size}}
     [:div.image
      [:img {:src (:url img)
             :style {:width size
                     :height size}}]]]))

(defn posts-images []
  (let [posts (subscribe [:posts])]
    (fn []
      [:div.row
       [:div.ui.link.cards
        (map post @posts)]])))

(defn- coordinates [post]
  "Extracts [lng lat] coordinates pair from post"
  (when-let [location (:location post)]
    [(:longitude location) (:latitude location)]))

(defn posts-map []
  (let [posts  (subscribe [:posts])
        points (reaction (remove nil? (map coordinates @posts)))]
    (log/debug "Loaded json" @points)
    (fn [] [mapbox-stateful {:style {:height 300}
                             :points @points
                             :zoom 10
                             :center {:lat 55.738471953
                                      :lng 37.588730507}}])))

