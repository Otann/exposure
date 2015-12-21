(ns exposure.views.pages
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.ratom :refer-macros [reaction]]
            [taoensso.timbre :as log]

            [exposure.routes :refer [url-for]]
            [exposure.views.components :as c]))

(defn welcome-page []
  [:div.jumbotron
   [:h1 "Welcome to Exposure"]
   [:p "This is web application for browsing Instagram data by hashtags"]
   [:a.btn.btn-primary {:href "/api/auth"} "Sign In with Instagram"]])

(defn app-page []
  (let [profile  (subscribe [:profile])
        username (reaction (-> @profile :user :username))
        token    (reaction (-> @profile :access_token))]
    (fn []
      [:div
       [:h1 "Welcome " @username]
       [:p "Your current access token: " [:code @token]]
       [c/search-form]])))

(defn home-page []
  (let [authorized (subscribe [:is-authorized])]
    (fn []
      (if @authorized
        [app-page]
        [welcome-page]))))

(defn page-nav
  "Component that displays link to the page in navbar"
  [active-page page-id name]
  [:li (when (= active-page page-id) {:class "active"})
   [:a {:href (url-for page-id)} name]])

(defn navbar []
  (let [profile (subscribe [:profile])
        active-page (subscribe [:active-page])]
    (fn []
      [:nav.navbar.navbar-default.navbar-fixed-top          ;.navbar-inverse
       [:div.container
        [:div.navbar-header
         [:a.navbar-brand {:href (url-for :home-page)}
          [:span.mega-octicon.octicon-broadcast]
          " Exposure"]]
        [:div.collapse.navbar-collapse
         [:ul.nav.navbar-nav]

         [:ul.nav.navbar-nav.navbar-right
          [page-nav @active-page :about-page "About"]
          (when @profile
            [:li [:a {:href (url-for :home)
                      :on-click #(dispatch [:sign-out])}
                  "Sign out"]])]]]])))

(defmulti page-for identity)
(defmethod page-for :home-page  [] [home-page])
(defmethod page-for :about-page [] [:div [:h1 "This is the About Page!"]])
(defmethod page-for :default    [] [:div [:h1 "404?!"]])
