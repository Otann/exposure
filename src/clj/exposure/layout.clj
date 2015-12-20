(ns exposure.layout
  (:require [environ.core :refer [env]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [cheshire.core :as json]

            [exposure.instagram-api :as instagram]))

(def reagent-mount-target [:div#app [:div.signal]])

(def reagent-page
  (html
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      (include-css "//cdn.jsdelivr.net/primer/2.5.0/primer.css")
      (include-css "//cdn.jsdelivr.net/octicons/3.3.0/octicons.css")
      (include-css (if (env :dev) "css/site.css" "css/site.min.css"))]
     [:body
      reagent-mount-target
      (include-js "js/app.js")]]))

(def auth-page
  (html
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      (include-css (if (env :dev) "css/site.css" "css/site.min.css"))]
     [:body
      [:div#app
       [:h3 "Please authorize with your Instagram account"]
       [:a {:href (instagram/auth-url)} "click here"]]]]))

(defn persist-and-redirect
  "Saves data to localStorage and redirects to provided URL"
  [{:keys [key data url]}]
  (let [json-data (json/generate-string data)
        script (str "localStorage.setItem('" key "', JSON.stringify(" json-data "));"
                    "window.location = '" url "';")]
    (html
      [:html
       [:head [:meta {:charset "utf-8"}]]
       [:body [:script script ]]])))