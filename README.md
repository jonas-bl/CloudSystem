# CloudSystem
Network & Server Management System for Minecraft Networks.



## Features
- Easy to use
- Multi Root Support
- REST API (comming soon)
- CloudAPI for integration in plugins
- Dynamic start of (Game) servers
- Dynamic Player-To-Lobby sending
- Server Signs
## Requirements
- Java 8 (or newer)
- **Note**: for Minecraft versions below 1.17 you require Java 8
- **Only compatible with Linux** (Debian or Ubuntu recommended)
- Screen (check installation & setup section if not installed)
- Minimum of 2 vCores and 4GB RAM recommended
## Installation & Setup

**Very detailed installation setup:**

**1. Install Java**
- If you have multiple Java versions installed, you can switch it using
```bash
  update-alternatives --config java
```
- Check if the selected version is active
```bash
  java -version
```

**2. Install screen**
```bash
  apt-get install screen
```

**3. Create a directory for the CloudSystem**
```bash
  cd /home
  mkdir CloudSystem #'CloudSystem' is just a placeholder, choose whatever you like
```

**4. Upload CloudSystem.jar to directory**
- To check if the .jar file is uploaded use
```bash
  cd /CloudSystem  #folder where 'CloudSystem.jar' should be located
  ls  #List all files in directory, 'CloudSystem.jar' should appear
```

**5. Create a start scripts**
- Create & edit commander start script:
```bash
  nano commander.sh
```
- Content of the file:
```bash
  screen -S Commander java -jar CloudSystem.jar --module=COMMANDER
```
- Create & edit Slave start script:
```bash
  nano slave.sh
```
- Content of the file:
```bash
  screen -S Slave-1 java -jar CloudSystem.jar --module=SLAVE
```
**Save & exit each file after pasting the code**

**6. First startup**
- Start the commander using './commander.sh'
- It will fail to start because you first have to setup the CloudSystem

**7. Setup CloudSystem**
- Create a new Slave (e.g. named 'Slave-1') in the Commander screen:
```bash
  syntax: create slave SLAVE_NAME CURRENT_IP_ADDRESS AMOUNT_OF_RAM_IN_MB
  example: create slave Slave-1 MY_IP_HERE 4096
```
- Create the server template 'Lobby' and 'Proxy' so players can connect
```bash
  syntax: Enter 'create' in console
  for Lobby: create template spigot Lobby 512 1 Slave-1
  for Proxy: create template proxy Proxy 512 1 Slave-1
```
- Shutdown the commander using CTRL+C or typing 'end'
- Upload BungeeCord.jar and Spigot.jar to the Global folder in '/templates/Global/Proxy' and '/templates/Global/Spigot'
- Upload a valid 'server.properties' and 'spigot.yml' file in the 'Global/Spigot' folder with onlinemode disabled / bungeecord enabled
- Start commander.sh and slave.sh







    
## Notes & Limitations
- There can only be one Slave per Root / VPS Server
- All servers of a template can only be started on the server of its template slave
- Commands can only be executed in the commander (thats what its name already implies)
- This CloudSystem was running and has been tested with over 250 players online at the same time
- In order for the system to work, you need a license provided by me.
