(ns exposure.views.components
  (:require [reagent.ratom :as r :refer-macros [reaction]]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]
            [ajax.core :refer [GET POST]]

            [exposure.routes :refer [url-for]]))

(defn search-form []
  (let [data (r/atom "")]
    (fn []
      [:form.form-inline
       {:on-submit (fn [e] (do (doto e
                                 (.preventDefault)
                                 (.stopPropagation))
                               (log/debug "Data is " @data)))}
       [:div.form-group
        [:label {:for "searchBox"} "Tag name"] " "
        [:input {:id "searchBox" :type "text"
                 :class "form-control"
                 :placeholder "getexposure"
                 :value @data
                 :on-change #(reset! data (-> % .-target .-value))}]] " "
       [:button.btn.btn-default {:type "submit"} "Search"]])))

