(ns exposure.views.pages
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [exposure.routes :refer [url-for]]
            [taoensso.timbre :as log]))

(defn home-page []
  (let [profile  (re-frame/subscribe [:profile])
        username (reaction (-> @profile :user :username))
        token    (reaction (-> @profile :access_token))]
    (fn []
      (if-not @profile
        [:div.jumbotron
         [:h1 "Welcome to Exposure"]
         [:p "This is web application for browsing Instagram data by hashtags"]
         [:a.btn.btn-primary {:href "/api/auth"} "Sign In with Instagram"]]

        [:div
         [:h1 "Welcome " @username]
         [:p "Your current access token:"]
         [:pre @token]]))))

(defn page-nav
  "Component that displays "
  []
  (let [active-page (re-frame/subscribe [:active-page])]
    (fn [page-id name]
      (log/debug "Active page" @active-page "page-id" page-id)
      [:li (when (= @active-page page-id) {:class "active"})
       [:a {:href (url-for page-id)} name]])))

(defn navbar []
  (let [profile (re-frame/subscribe [:profile])]
    (fn []
      [:nav.navbar.navbar-default.navbar-fixed-top          ;.navbar-inverse
       [:div.container
        [:div.navbar-header
         [:a.navbar-brand {:href (url-for :home-page)}
          [:span.mega-octicon.octicon-broadcast]
          " Exposure"]]
        [:div.collapse.navbar-collapse
         [:ul.nav.navbar-nav
          #_[page-nav :home-page "Home"]]

         [:ul.nav.navbar-nav.navbar-right
          [page-nav :about-page "About"]
          (when @profile
            [:li [:a {:href (url-for :home)
                      :on-click #(re-frame/dispatch [:sign-out])}
                  "Sign out"]])]]]])))

(defmulti page-for identity)
(defmethod page-for :home-page  [] [home-page])
(defmethod page-for :about-page [] [:div [:h1 "This is the About Page!"]])
(defmethod page-for :default    [] [:div [:h1 "404?!"]])
