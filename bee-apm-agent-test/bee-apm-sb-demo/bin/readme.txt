ps x | grep beeapm | grep -v grep | awk '{print $1}' | xargs kill -9
ps x | grep startup | grep -v grep | awk '{print $1}' | xargs kill -9
