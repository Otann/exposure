(ns exposure.views
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [taoensso.timbre :as log]
            [exposure.routes :refer [url-for]]
            [exposure.views.pages :as pages]))

(defn root-component []
  (let [active-page (re-frame/subscribe [:active-page])]
    (fn []
      [:div
       [pages/navbar]
       [:div.container
        (pages/page-for @active-page)]])))
