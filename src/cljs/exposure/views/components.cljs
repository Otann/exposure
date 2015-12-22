(ns exposure.views.components
  (:require [reagent.ratom :as r :refer-macros [reaction]]
            [re-frame.core :refer [dispatch subscribe]]
            [exposure.routes :refer [url-for]]
            [taoensso.timbre :as log :include-macros true]))


(defn search-form []
  (let [data (subscribe [:search-input])]
    (fn [token]
      [:div.ui.action.input
       [:input {:type "text"
                :value @data
                :on-change #(dispatch [:search-input (-> % .-target .-value)])}]
       [:button.ui.button {:on-click #(dispatch [:search-init @data token])}
        "Search"]])))

(defn post [data]
  (let [img (-> data :images :low_resolution)
        size "8em"]
    ^{:key (:id data)}
    [:div.six.doubling.card
     {:style {:width size}}
     [:div.image
      [:img {:src (:url img)
             :style {:width size
                     :height size}}]]]))

(defn posts [data]
  [:div.row
   [:div.ui.link.cards
    (map post data)]])


