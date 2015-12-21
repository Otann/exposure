(ns exposure.views.components
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [exposure.routes :refer [url-for]]))

(defn search-component
  [:div.search
   [:p "OLOLO"]])
