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
  ::defaults
  (fn [db]
    (:default db)))

(re-frame/reg-sub
  ::form-field-defs
  (fn [db]
    (:form-field-defs db)))
