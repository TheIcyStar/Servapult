# Autocrafter
Lets you launch several different Minecraft servers from discord. Also pushes minecraft chats to a discord channel

## Server configs
Example config:
```yaml
servers:
  latest: # Server slug
    directory: path/to/server/directory
    cmd: # Command and arguments to run to start the server
      - java
      - -Xmx1024M
      - -Xms1024M
      - -jar
      - server.jar
    type: "minecraft" # Used for handling startup notifications, chat messages, and stop commands (graceful shutdowns)
    shutdowns: # Optional, omit for no auto shutdowns
      grace-minutes: 30
  astral:
    directory: path/to/other/server/directory
    cmd: 
      - java
      - -Xmx1024M
      - -Xms1024M
      - -jar
      - server.jar
      - -nogui
    type: "minecraft"
    shutdowns: 
      grace-minutes: 30
```

## Environment variables
`SERVICE_TYPE` - "STARTER", "HOSTER", or "BOTH". The STARTER client will ping the HOSTER client to spin up servers. BOTH spins up both instances on the same machine
`DISCORD_BOT_TOKEN` - Discord bot api token
`DISCORD_SERVER_ID` - Snowflake ID of the bot's server
`CONFIG_PATH` - Path to the server config file
`SERVER SLUGS` - A comma separated string of servers available on the HOSTER