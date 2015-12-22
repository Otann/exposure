(ns exposure.views.pages
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.ratom :refer-macros [reaction]]
            [taoensso.timbre :as log]

            [exposure.routes :refer [url-for]]
            [exposure.views.components :as c]))

(defn welcome-page []
  [:div.ui.vertical.masthead.center.aligned.segment
   {:style {:min-height "500px"}}
   [:div.ui.test.container
    [:h1.header {:style {:margin-top "3em"
                                  :font-size "4em"}}
     "Welcome to Exposure"]
    [:h3 "Web application to browse Instagram data by hashtags"]
    [:a.ui.huge.primary.button {:href "/api/auth"}
     "Start browsing"
     [:i.icon.right.arrow]]]])

(defn app-page []
  (let [profile  (subscribe [:profile])
        username (reaction (-> @profile :user :username))
        token    (reaction (-> @profile :access_token))
        posts    (subscribe [:posts])]
    (fn []
      [:div.ui.grid.container
       [:div.row [:h1 "Welcome, dear " @username]]
       [:div.row [:p "Your current access token: " [:code @token]]]
       [:div.row [c/search-form @token]]
       [:div.row [c/posts @posts]]])))

(defn home-page []
  (let [authorized (subscribe [:is-authorized])]
    (fn []
      (if @authorized
        [app-page]
        [welcome-page]))))

(defn navbar []
  (let [profile (subscribe [:profile])]
    (fn []
      [:div.ui.attached.stackable.menu
       [:div.ui.container
        [:a.item {:href (url-for :home-page)}
         [:i.octicon.octicon-broadcast {:style {:margin-right 5}}]
         "Exposure"]

        [:div.right.menu
         [:a.ui.item {:href (url-for :about-page)}
          "About"]

         (if @profile
           [:a.ui.item {:href (url-for :home) :on-click #(dispatch [:sign-out])}
            "Sign Out"]
           [:a.ui.item {:href "/api/auth"}
            "Sign In"])]]])))

(defmulti page-for identity)
(defmethod page-for :home-page  [] [home-page])
(defmethod page-for :about-page [] [:div [:h1 "This is the About Page!"]])
(defmethod page-for :default    [] [:div [:h1 "404?!"]])
