(ns pwdgenerator-reframe.views
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.subs :as subs]
    [clojure.string :as s]))

;; home

(defmacro log
  [& msgs]
  `(.log js/console ~@msgs))

(def password-validations
  [["At least 12 characters"
    (fn [s]
      (>= (count s) 12))]
   ["At least 50% unique characters"
    (fn [s]
      (-> s
          set
          count
          (/ (count s))
          (>= 0.5)))]])

(defn on-field-change [s field event]
  (do (swap! s assoc field (-> event .-target .-value))
      (re-frame/dispatch [:generate @s])))

(defn form-field [field s]
  (let [form-field-defs @(re-frame/subscribe [::subs/form-field-defs])
        defs (field form-field-defs)]
    [:div {:id (str field "-input")}
     [:label (:label defs)
      [:input {:type      :text
               :size      (:size defs)
               :maxLength (:maxlength defs)
               :value     (field @s)
               :on-change #(on-field-change s field %)}]]]))

(defn form-fields [s]
  (let [form-field-defs @(re-frame/subscribe [::subs/form-field-defs])]
   (map #(form-field % s) (sort-by #(:order (% form-field-defs)) (keys form-field-defs)))))

(defn random-char [s]
  (nth s (rand-int (count s))))

(defn random-string [len s]
  (let [length (js/parseInt len)]
    (apply str (take length (repeatedly #(random-char s))))))

(defn uppercase-word [params]
  (random-string (:no_uppercase_alpha params) (:uppercase params)))

(defn lowercase-word [params]
  (random-string (:no_lowercase_alpha params) (:lowercase params)))

(defn numerics-symbols-word [params]
  (apply str
         (shuffle
           (seq
             (str (random-string (:no_numerics params) (:numerics params))
                  (random-string (:no_symbols params) (:symbols params)))))))

(defn all-words [params]
  (let [generators [uppercase-word lowercase-word numerics-symbols-word]
        words (js/parseInt (:no_words params))]
    (shuffle
      (take words (repeatedly #((nth generators (rand-int (count generators))) params))))))

(defn generate-pw [params]
  (s/join (:word_separator params) (all-words params)))

(re-frame/reg-event-db
  :initialize
  (fn [_ _]
    (let [defaults @(re-frame/subscribe [::subs/defaults])]
      {:value (generate-pw defaults)})))

(re-frame/reg-event-db
  :generate
  (fn [db [_ s]]
    (assoc db :value (generate-pw s))))

(re-frame/reg-sub
  :value
  (fn [db] (:value db)))

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
    (defonce _init (re-frame/dispatch-sync [:initialize]))
    [show-panel @active-panel]))
