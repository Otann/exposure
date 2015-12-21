(ns exposure.views
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [taoensso.timbre :as log]
            [exposure.routes :refer [url-for]]))


;; home

(defn home-page []
  (let [profile  (re-frame/subscribe [:profile])
        username (reaction (-> @profile :user :username))
        token    (reaction (-> @profile :access_token))]
    (fn []
      (if-not @profile
        [:div.blankslate.clean-background
         [:h1 "Welcome to Exposure"]
         [:p "This is web application for browsing Instagram data by hashtags"]
         [:a.btn.btn-primary {:href "/api/auth"} "Sign In with Instagram"]]

        [:div
         [:h1 "Welcome " @username]
         [:p "Your current access token:"]
         [:pre @token]]))))

(defn header []
  (let [profile (re-frame/subscribe [:profile])
        picture (reaction (-> @profile :user :profile_picture))]
    (fn []
      [:header.masthead
       [:div.container
        [:a.masthead-logo {:href (url-for :home)}
         [:span.mega-octicon.octicon-broadcast]
         "Exposure"]
        [:nav.masthead-nav
         [:a {:href (url-for :about)} "About"]
         (when @profile
           [:a {:href (url-for :home)
                :on-click #(re-frame/dispatch [:sign-out])}
            "Sign out"
            [:img.avatar.avatar-small {:src @picture}]])]]])))

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
       [:div.container
        (pages @active-page)]])))
