SCRIPT_DIR=$(dirname "$(readlink -f -- "$0")")
SERVER_DIR=${SCRIPT_DIR}/build/Server

red="\e[0;31m"
green="\e[0;32m"
blue="\e[0;34m"
endc="\e[0m"

# Make sure that the server has been built.
if ! [ -f ${SERVER_DIR}/app.yaml ]; then
  echo -e "${red}Error: The server has not been built.${endc}"
  echo "You need to run buildServer.sh."
  exit 1
fi

# Make sure that the App Engine dev server is available.
which dev_appserver.py  1> /dev/null 2> /dev/null
if [ $? -ne 0 ]; then
  echo -e "${red}Error: Could not find the App Engine development server.${endc}"
  echo "You need to install Google App Engine SDK for Python."
  echo "See https://cloud.google.com/appengine/downloads#Google_App_Engine_SDK_for_Python"
  exit 1
fi

# Start the App Engine dev server.
echo -e "${green}Starting the App Engine development server.${endc}"
echo -e "${blue}http://localhost:8080/${endc}"
echo
dev_appserver.py ${SERVER_DIR}/
