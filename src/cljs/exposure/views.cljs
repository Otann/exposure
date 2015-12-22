(ns exposure.views
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [subscribe dispatch]]
            [exposure.routes :refer [url-for]]

            [exposure.views.navbar :refer [navbar]]
            [exposure.views.pages :refer [active-page]]))

(defn root-component []
  [:div
   [navbar]
   [active-page]])
