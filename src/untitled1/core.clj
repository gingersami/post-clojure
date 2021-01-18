(ns untitled1.core
  (:require [clojure.data.json :as json])
  (:require [clojure.core.strint :refer [<<]])
  (:require [cljc.java-time.format.date-time-formatter :as date-formatter])
  (:require [cljc.java-time.local-date-time :as date-time])
  (:gen-class)
  )

(def map-array [{
                 :name     "GCP_INT"
                 :services "https://gservices-int.traxretail.com"
                 :internal "https://internal-gservices-int.trax-cloud.com"
                 }
                {
                 :name     "INT"
                 :services "https://services-int.traxretail.com"
                 :internal "https://internal-services-int.trax-cloud.com"
                 }
                {
                 :name     "PROD"
                 :services "https://services.traxretail.com"
                 :internal "https://internal-services.trax-cloud.com"
                 }
                {
                 :name     "GCP_PROD"
                 :services "https://gservices.traxretail.com"
                 :internal "https://internal-gservices.trax-cloud.com"
                 }
                {
                 :name     "GCP_EDGE"
                 :services "https://gservices-edge.traxretail.com"
                 :internal "https://internal-gservices-edge.trax-cloud.com"
                 }
                {
                 :name     "EDGE"
                 :services "https://services-edge.traxretail.com"
                 :internal "https://internal-services-edge.trax-cloud.com"
                 }])

(defn get-time []
    (date-time/format
      (date-time/now)
      date-formatter/iso-date-time)
  )

(defn write-file [env-map]
  (let [new-has {
                 :id                       (str (java.util.UUID/randomUUID))
                 :name                     (get env-map :name)
                 :values                   (seq [{
                                                  :key     "SERVICES_URL"
                                                  :value   (get env-map :services)
                                                  :enabled true
                                                  }
                                                 {
                                                  :key     "INTERNAL_URL"
                                                  :value   (get env-map :internal)
                                                  :enabled true
                                                  }
                                                 ])
                 "_postman_variable_scope" "environment",
                 "_postman_exported_at"    (get-time),
                 "_postman_exported_using" "Postman/7.36.1"
                 }]
    (spit (<< "./~(get env-map :name).json") (json/write-str new-has))
    )
  )

(defn create-environments []
  (doseq [n map-array]
    (write-file n))
  )
(defn -main [& args]
  (create-environments)
  )
