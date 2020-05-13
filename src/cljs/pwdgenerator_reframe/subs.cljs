(ns pwdgenerator-reframe.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
  ::params
  (fn [db]
    (:params db)))

(re-frame/reg-sub
  ::field-defs
  (fn [db]
    (:field-defs db)))

(re-frame/reg-sub
  ::value
  (fn [db] (:value db)))

(re-frame/reg-sub
  ::show?
  (fn [db] (:show? db)))

(re-frame/reg-sub
  ::user
  (fn [db] (:user db)))
