(ns exposure.views
  (:require [re-frame.core :as re-frame]
            [taoensso.timbre :as log]
            [exposure.routes :refer [url-for]]))


;; home

(defn home-page []
  (let [profile (re-frame/subscribe [:profile])]
    (fn []
      (if @profile
        [:div
         [:h1 "Welcome " (-> @profile :user :username)]]

        [:div {:class "blankslate clean-background"}
         [:h1 "Welcome to Exposure"]
         [:p "This is web application for browsing Instagram data by hashtags"]
         [:a {:class "btn btn-primary"
              :href "/api/auth"} "Sign In with Instagram"]]))))

(defn header []
  (let [profile (re-frame/subscribe [:profile])]
    (fn []
      [:header {:class "masthead"}
       [:div {:class "container"}
        [:a {:class "masthead-logo" :href (url-for :home)}
         [:span {:class "mega-octicon octicon-broadcast"}]
         "Exposure"]
        [:nav {:class "masthead-nav"}
         [:a {:href (url-for :about)} "About"]
         (when @profile
           [:a {:href (url-for :home)
                :on-click #(re-frame/dispatch [:sign-out])}
            "Sign out"
            [:img {:class "avatar avatar-small"
                   :src (-> @profile :user :profile_picture)}]])]]])))

;; main

(defmulti pages identity)
(defmethod pages :home-page  [] [home-page])
(defmethod pages :about-page [] [:div [:h1 "This is the About Page!"]])
(defmethod pages :default    [] [:div [:h1 "404?!"]])

(defn root-component []
  (let [active-page (re-frame/subscribe [:active-page])]
    (fn []
      [:div
       [header]
       [:div {:class "container"}
        (pages @active-page)]])))
