(ns exposure.layout
  (:require [environ.core :refer [env]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [cheshire.core :as json]))

(def reagent-page
  (html
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:title (when (env :dev) "DEV - ") "Exposure App"]

      #_(include-css "//cdn.jsdelivr.net/primer/2.5.0/primer.css")
      (include-css "//cdn.jsdelivr.net/octicons/3.3.0/octicons.css")

      #_(include-css "//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css")
      #_(include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css")

      (include-css "//oss.maxcdn.com/semantic-ui/2.1.7/semantic.min.css")

      (include-css (if (env :dev) "css/site.css" "css/site.min.css"))]
     [:body
      [:div#app [:div.signal]]
      (include-js "js/app.js")]]))

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