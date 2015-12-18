(ns exposure.layout
  (:require [environ.core :refer [env]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]))

(def mount-target
  [:div#app
   [:h3 "ClojureScript has not been compiled!"]
   [:p "please run "
    [:b "lein figwheel"]
    " in order to start the compiler"]])

(def loading-page
  (html
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport"
              :content "width=device-width, initial-scale=1"}]
      (include-css (if (env :dev)
                     "css/site.css"
                     "css/site.min.css"))]
     [:body
      mount-target
      (include-js "js/app.js")]]))
