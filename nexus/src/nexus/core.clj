(ns nexus.core
  (:require [clj-http.client :as http]
            [clojure.data.zip.xml :refer [text attr xml-> xml1->]]
            [clojure.zip :as zip]
            [clojure.data.xml :as xml]))

(defn mk-url
  "Create a url to search nexus for the specified artifact id"
  [id]
  (format
    "http://nexus.containerstore.com/nexus/service/local/lucene/search?a=%s" id))

(defn fetch-meta
  "Grab the raw xml data for the artifact from nexus"
  [id]
  (->
    id
    mk-url
    http/get
    :body))

(defn parse-meta
  "Convert the artifact xml into a zipper for
  convenient processing."
  [xml]
  (-> xml
      xml/parse-str
      zip/xml-zip))

(defn latest-version
  "Given a zipper root, extract the latest release version"
  [root]
  (->
    (map
      #(xml1-> % :artifact :latestRelease text)
      (xml-> root :data))
    first))

(defn find-version
  "Query nexus for the latest version of the specified artifact id"
  [id]
  (->
    id
    fetch-meta
    parse-meta
    latest-version))

;;
;; REPL experiments
;;
(comment
  (def t-id "common-base")

  (def z-data (->
                t-id
                core/fetch-meta
                core/parse-order)))