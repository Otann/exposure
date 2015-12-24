(ns exposure.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub :active-page
  (fn [db _] (reaction (:active-page @db))))

(register-sub :profile
  (fn [db _] (reaction (:profile @db))))

(register-sub :is-authorized
  (fn [db _] (reaction (boolean (:profile @db)))))

(register-sub :search-input
  (fn [db _] (reaction (:search-input @db))))

(register-sub :posts
  (fn [db _] (reaction (:posts @db))))
