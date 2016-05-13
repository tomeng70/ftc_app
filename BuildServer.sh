SCRIPT_DIR=$(dirname "$(readlink -f -- "$0")")
SERVER_DIR=${SCRIPT_DIR}/build/Server

red="\e[0;31m"
green="\e[0;32m"
blue="\e[0;34m"
endc="\e[0m"

# Create the server directory.
[ -d ${SERVER_DIR} ] || mkdir -p ${SERVER_DIR}
if [ $? -ne 0 ]; then
  echo -e "${red}Error: Could not create the server directory.${endc}"
  exit 1
fi

# Create a symbolic link to ftcblocks.py, which contains the python server code.
[ -L ${SERVER_DIR}/ftcblocks.py ] || ln -s ${SCRIPT_DIR}/Server/ftcblocks.py ${SERVER_DIR}/ftcblocks.py
if [ $? -ne 0 ]; then
  echo -e "${red}Error: Could not create a symbolic link to ftcblocks.py.${endc}"
  exit 1
fi

# Create a symbolic link to assets, which contains the HTML and JS files for the blocks
# environment.
[ -L ${SERVER_DIR}/assets ] || ln -s ${SCRIPT_DIR}/FtcRobotController/src/main/assets ${SERVER_DIR}/assets
if [ $? -ne 0 ]; then
  echo -e "${red}Error: Could not create a symbolic link to assets.${endc}"
  exit 1
fi

# Create a symbolic link to app.yaml, which configures the App Engine application's settings.
[ -L ${SERVER_DIR}/app.yaml ] || ln -s ${SCRIPT_DIR}/Server/app.yaml ${SERVER_DIR}/app.yaml
if [ $? -ne 0 ]; then
  echo -e "${red}Error: Could not create a symbolic link to app.yaml.${endc}"
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

echo -e "${green}BUILD SUCCESSFUL${endc}"
