(ns pwdgenerator-reframe.views
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.domain :refer [password-validations]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]
    [pwdgenerator-reframe.config :as config]))

;; home

(defn on-field-change [params field event]
  (do (assoc params field (-> event .-target .-value))
      (re-frame/dispatch [::events/generate])))

(defn form-field [field params]
  (let [field-defs @(re-frame/subscribe [::subs/field-defs])
        defs (field field-defs)]
    [:div {:id (str field "-input")}
     [:label (:label defs)
      [:input {:type      :text
               :size      (:size defs)
               :maxLength (:maxlength defs)
               :value     (field params)
               :on-change #(on-field-change params field %)}]]]))

(defn form-fields [params]
  (let [field-defs @(re-frame/subscribe [::subs/field-defs])]
    (map #(form-field % params) (sort-by #(:order (% field-defs)) (keys field-defs)))))

(defn pwdgenerator []
  (let [defaults @(re-frame/subscribe [::subs/defaults])
        s (reagent/atom (merge defaults {:show? true}))]
    (fn []
      (let [value @(re-frame/subscribe [:value])
            validations (for [[desc f] password-validations]
                          [desc (f (:value @s))])
            valid? (every? identity (map second validations))
            color (when (:dirty? @s) (if valid? "green" "red"))]
        [:form
         [:div {:id :dbdump} (pr-str @s)]
         [:label {:style {:color color}} "Password"]
         [:input {:type      (if (:show? @s) :text :password)
                  :style     {:width  "100%"
                              :border (str "1px solid " color)}
                  :value     value
                  :on-focus  #(swap! s assoc :focus? true)
                  :on-blur   #(swap! s assoc :dirty? true)
                  :on-change #(swap! s assoc
                                     :dirty? true
                                     :value
                                     (-> % .-target .-value))}]
         [:div {:id "show-password-input"}
          [:label [:input {:type      :checkbox
                           :checked   (:show? @s)
                           :on-change #(swap! s assoc
                                              :show?
                                              (-> % .-target .-checked))}]
           " Show password?"]]
         (form-fields s)
         [:div {:id "word-separator-input"}
          [:label "Word Separator "
           [:input {:type      :text
                    :size      3
                    :maxLength 3
                    :value     (:word_separator @s)
                    :on-change #(on-field-change s :word_separator %)}]
           " " (pr-str (:word_separator @s))]]
         [:div {:id :regenerate :on-click
                    (fn []
                      (re-frame/dispatch [:generate @s]))} "Regenerate"]
         (for [[desc valid?] validations]
           (when (:focus? @s)
             [:div {:style {:color (when (:dirty? @s)
                                     (if valid? "green" "red"))}}
              (when (:dirty? @s) (if valid? "✔ " "✘ "))
              desc]))]))))

(defn home-panel []
  (let [name @(re-frame/subscribe [::subs/name])]
    [:div
     [:h1 (str "Hello from " name ". This is the Home Page.")]
     [pwdgenerator]

     [:div
      [:a {:href "#/about"}
       "go to About Page"]]
     ]))

;; about

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:href "#/"}
     "go to Home Page"]]])

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
