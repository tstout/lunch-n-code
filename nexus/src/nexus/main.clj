(ns nexus.main
  (:gen-class)
  (:require [nexus.core :refer [find-version]]
            [clojure.string :as string]
            [clojure.tools.cli :as cli]))

(def options
  [["-a" "--artifact-id" "Maven artifact id"]
   ["-h" "--help"]])

(defn usage [option-summary]
  (->>
    ["Find the latest version of a maven artifact available in TCS nexus"
     ""
     "Options:"
     option-summary]
    (string/join \newline)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn error-msg [errors]
  (str "Error while parsing options:\n\n"
       (string/join \newline errors)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args options)]
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      (:artifact-id options) (println (find-version (first arguments)))
      errors (exit 1 (error-msg errors)))))

