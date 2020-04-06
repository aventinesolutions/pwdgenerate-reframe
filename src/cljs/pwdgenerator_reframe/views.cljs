(ns pwdgenerator-reframe.views
  (:require
    [cljs.pprint :refer [pprint]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.domain :refer [password-validations]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]))

;; home

(defn on-field-change [params field event]
  (re-frame/dispatch [::events/params (assoc params field (-> event .-target .-value))]))

(defn form-field [field]
  (let [params (re-frame/subscribe [::subs/params])
        field-defs (re-frame/subscribe [::subs/field-defs])
        defs (field @field-defs)]
    ^{:key field}
    [:div {:id (str field "-input") :class [:uk-padding-small :uk-padding-remove-bottom]}
     [:label (:label defs)
      [:input {:type      :text
               :size      (:size defs)
               :maxLength (:maxlength defs)
               :value     (field @params)
               :on-change #(on-field-change @params field %)}]]]))

(defn form-fields []
  (let [field-defs (re-frame/subscribe [::subs/field-defs])]
    (map #(form-field %) (sort-by #(:order (% @field-defs)) (keys @field-defs)))))

(defn pwdgenerator []
  (let [value (re-frame/subscribe [::subs/value])
        params (re-frame/subscribe [::subs/params])
        show? (re-frame/subscribe [::subs/show?])
        dirty? (re-frame/subscribe [::subs/dirty?])
        focus? (re-frame/subscribe [::subs/focus?])]
    (fn []
      (let [validations (for [[desc f] password-validations]
                          [desc (f (:value @value))])
            valid? (every? identity (map second validations))
            color (when @dirty? (if valid? "green" "red"))]
        [:form {:class [:uk-card :uk-card-default :uk-card-body :uk-padding]}
         [:div {:id :dbdump} (pr-str @params)]
         [:label {:style {:color color}} "Password"]
         [:input {:type      (if @show? :text :password)
                  :style     {:width  "100%"
                              :border (str "1px solid " color)}
                  :value     @value
                  :on-focus  #(re-frame/dispatch [::events/focus? true])
                  :on-blur   #(re-frame/dispatch [::events/dirty? true])
                  :on-change #(doall (re-frame/dispatch [::events/dirty? true])
                                     (re-frame/dispatch [::events/value (-> % .-target .-value)]))}]
         [:div {:id "show-password-input"}
          [:label [:input {:type      :checkbox
                           :checked   @show?
                           :on-change #(re-frame/dispatch [::events/show? (-> % .-target .-checked)])}]
           " Show password?"]]
         (doall (form-fields))
         ^{:key "word-separator-input"}
         [:div {:id "word-separator-input"}
          [:label "Word Separator "
           [:input {:type      :text
                    :size      3
                    :maxLength 3
                    :value     (:word_separator @params)
                    :on-change #(on-field-change @params :word_separator %)}]
           " " (pr-str (:word_separator @params))]]
         ^{:key "regenerate"}
         [:button {:id :regenerate :class [:uk-button :uk-button-small :uk-button-primary] :on-click
                       (fn [event]
                         (do
                           (.preventDefault event)
                           (re-frame/dispatch [::events/generate])))} "Regenerate"]
         ^{:key "reset"}
         [:button {:id :reset :class [:uk-button :uk-button-small :uk-button-secondary] :on-click
                       (fn [event]
                         (do
                           (.preventDefault event)
                           (re-frame/dispatch [::events/reset])))} "Reset"]
         (doall
           (for [[desc valid?] validations]
             (when focus?
               ^{:key desc}
               [:div {:style {:color (when @dirty?
                                       (if valid? "green" "red"))}}
                (when @dirty? (if valid? "✔ " "✘ "))
                desc])))]))))

(defn home-panel []
  (let [name @(re-frame/subscribe [::subs/name])]
    [:div {:class [:uk-flex :uk-flex-center :uk-flex-top :uk-padding]}
     [:h1 {:class [:uk-text-lead :uk-card :uk-card-default :uk-padding]} name]
     [pwdgenerator]

     [:div {:class [:uk-text-small :uk-card :uk-card-default :uk-padding]}
      [:a {:class [:uk-link-muted] :href "#/about"}
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
