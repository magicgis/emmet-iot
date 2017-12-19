#! /bin/bash

ls -al /root/.node-red

#npm link /root/.node-red/emmet-workflow-nodes
cd /root/.node-red
node-red --userDir /root/.node-red

#PROFILES_ACTIVE
# echo PROFILES_ACTIVE: "$PROFILES_ACTIVE"
# if [ "$PROFILES_ACTIVE" == "dev"  ]; then
#   node-red --userDir /root/.node-red --settings /root/.node-red/settings_dev.js flows_emmet.json
# fi

# if [ "$PROFILES_ACTIVE" == "stage"  ]; then
#   node-red --userDir /root/.node-red --settings /root/.node-red/settings_staging.js flows_emmet.json
# fi

# if [ "$PROFILES_ACTIVE" == "production"  ]; then
#   node-red --userDir /root/.node-red --settings /root/.node-red/settings_production.js flows_emmet.json
# fi
