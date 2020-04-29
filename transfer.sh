#!/bin/bash
sshdir="/c/Users/Grant/.ssh/shadobot-keys.pem"
jarname="communismbot-1.0-SNAPSHOT.jar"
jardir="/c/Users/Grant/IdeaProjects/communismbot/flag.png"
todir="/home/ec2-user/flag.png"

scp -i "$sshdir" "$jardir" ec2-user@ec2-18-188-192-146.us-east-2.compute.amazonaws.com:"$todir"