(ns exposure.views.components
  (:require [reagent.ratom :as r :refer-macros [reaction]]
            [re-frame.core :refer [dispatch]]
            [exposure.routes :refer [url-for]]
            [taoensso.timbre :as log :include-macros true]))


(defn submit-search
  "Handles form submit for search-form"
  [js-event data token]
  (doto js-event
    (.preventDefault)
    (.stopPropagation))
  (dispatch [:search-init data token]))

(defn search-form []
  (let [data (r/atom "")]
    (fn [token]
      [:div.ui.action.input
       [:input {:type "text"
                :placeholder "getexposure"
                :value @data
                :on-change #(reset! data (-> % .-target .-value))}]
       [:button.ui.button {:on-click #(dispatch [:search-init @data token])}
        "Search"]])))

(defn post [data]
  (let [img (-> data :images :thumbnail)]
    ^{:key (:id data)}
    [:div.card
     {:style {:width (:width img)}}
     [:div.image
      [:img {:src (:url img)
             :style {:width (:width img)
                     :height (:height img)}}]]
     [:div.content
      [:div.description
       (str (-> data :caption :text))]]]))

(defn posts [data]
  [:div.row
   [:div.ui.link.cards
    (map post data)]])


