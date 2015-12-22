(ns exposure.views.components
  (:require [reagent.ratom :as r :refer-macros [reaction]]
            [re-frame.core :refer [dispatch]]
            [exposure.routes :refer [url-for]]))


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
      [:div.row
       [:form.form-inline
        {:on-submit #(submit-search % @data token)}
        [:div.form-group
         [:label {:for "searchBox"} "Tag name"] " "
         [:input {:id "searchBox" :type "text"
                  :class "form-control"
                  :placeholder "getexposure"
                  :value @data
                  :on-change #(reset! data (-> % .-target .-value))}]] " "
        [:button.btn.btn-default {:type "submit"} "Search"]]])))

(defn post [data]
  (let [img (-> data :images :thumbnail)]
    ^{:key (:id data)}
    [:div.col-xs-6.col-md-3
     [:a.thumbnail
      [:img {:src (:url img)
             :style {:width (:width img)
                     :height (:height img)}}]
      [:div.caption
       [:p (-> data :caption :text)]]]]))

(defn posts [data]
  [:div.row (map post data)])


