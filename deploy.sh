if [ "$1" = "dev" ]; then
  export host="dev4.domaingoeshere.com"
  export user="ecr"
  export pemfile="/Users/miguelesparzajr/dev4.pem"
  cp src/main/resources/application-dev.properties src/main/resources/application.properties
else
  if [ "$1" = "prod" ]; then
    export host="em"
    export user="er"
    export pemfile="~/tmh.pem"
    export proxyHost="dev4.nobleapplications.com"
    export proxyUser="ec2-user"
    export proxyPem="~/dev4.pem"
    cp src/main/resources/application-prod.properties src/main/resources/application.properties
  else
    echo "Please specify either 'dev' or 'prod'"
    exit 1
  fi
fi

echo "Deploying to $user@$host"

# Build the WAR file
./gradlew -Penv="$1" --warning-mode all build
if [ $? -ne 0 ]; then
  echo "Build failed!"
  exit 1
fi

# Upload the WAR file
if [ "$1" = "prod" ]; then
  rsync -Pav -e "ssh -o 'ProxyCommand=ssh -i $proxyPem -W %h:%p $proxyUser@$proxyHost' -i $pemfile" build/libs/tanner.war $user@$host:~/tanner.war
else
  rsync -Pav -e "ssh -i $pemfile" build/libs/rogerwi.war $user@$host:~/rogerwi.war
fi

if [ $? -ne 0 ]; then
  echo "Upload failed!"
  exit 1
fi

# Deploy it and restart Tomcat
if [ "$1" = "prod" ]; then
  ssh -o "ProxyCommand=ssh -i $proxyPem -W %h:%p $proxyUser@$proxyHost" -i $pemfile $user@$host "sudo service tomcat9 stop"
  ssh -o "ProxyCommand=ssh -i $proxyPem -W %h:%p $proxyUser@$proxyHost" -i $pemfile $user@$host "sudo rm -rf /usr/share/tomcat9/webapps/ROOT"
  ssh -o "ProxyCommand=ssh -i $proxyPem -W %h:%p $proxyUser@$proxyHost" -i $pemfile $user@$host "sudo cp tanner.war /usr/share/tomcat9/webapps/ROOT.war"
  ssh -o "ProxyCommand=ssh -i $proxyPem -W %h:%p $proxyUser@$proxyHost" -i $pemfile $user@$host "sudo service tomcat9 start"
  ssh -o "ProxyCommand=ssh -i $proxyPem -W %h:%p $proxyUser@$proxyHost" -i $pemfile $user@$host "sudo tail -f /var/log/tomcat9/catalina.`date -u +%Y`-`date -u +%m`-`date -u +%d`.log"
else
	ssh -i $pemfile $user@$host "sudo service tomcat9 stop"
	ssh -i $pemfile $user@$host "sudo rm -rf /usr/share/tomcat9/webapps/rogerwi"
	ssh -i $pemfile $user@$host "sudo cp rogerwi.war /usr/share/tomcat9/webapps/rogerwi.war"
	ssh -i $pemfile $user@$host "sudo service tomcat9 start"
	ssh -i $pemfile $user@$host "sudo ./log.sh" 
fi
