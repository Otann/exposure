(ns exposure.views
  (:require [re-frame.core :as re-frame]
            [exposure.routes :as routes]))


;; home

(defn home-page []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div
       [:h1 (str "Hello from " @name ". This is the Home Page.")]
       [:div [:a {:href "/about"} "go to About Page"]]
       [:div [:button {:class "btn"
                       :on-click #(re-frame/dispatch [:btn-clicked])}
              "Click me!"]]])))


;; about

(defn about-page []
  (let [clicks (re-frame/subscribe [:clicks])]
    (fn []
      [:div
       [:h1 "This is the About Page!"]
       [:p (str "Button on home was clicked " @clicks " times")]
       [:div [:a {:href "/"} "go to Home Page"]]])))

;; main

(defmulti pages identity)
(defmethod pages :home-page [] [home-page])
(defmethod pages :about-page [] [about-page])
(defmethod pages :default [] [:div [:h1 "404?!"]])

(defn root-component []
  (let [active-page (re-frame/subscribe [:active-page])]
    (fn []
      [:div
       [:header {:class "masthead"}
        [:div {:class "container"}
         [:a {:class "masthead-logo"}
          [:span {:class "mega-octicon octicon-gist-secret"}]
          "Investigrator"]]]
       [:div {:class "container"}
        (pages @active-page)]])))
