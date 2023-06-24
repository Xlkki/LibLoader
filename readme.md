<img align="right" src="https://media.discordapp.net/attachments/945691411435622453/1122117359134572615/download.png" height="140" width="140">

# LibLoader

LibLoader - plugin that allows you to load 
libraries from .jar files at runtime


# Usage 

To load a .jar file as a library, you need to 
put it in the folder `/plugins/LibLoader/libs/`.
The libraries are loaded when the plugin is enabled.


# Commands


## /library list 

Shows a list of loaded and available for 
loading libraries

<img src="https://media.discordapp.net/attachments/945691411435622453/1122120444749807676/image.png">


## /library load <file_name> 

Loads the library from the specified file
(the file should be in `/plugins/LibLoader/libs`)

<img src="https://media.discordapp.net/attachments/945691411435622453/1122123009696739378/image.png">

## /library unload <library_id>

Unloads the library with the specified id 
(file name without an extension)

<img src="https://media.discordapp.net/attachments/945691411435622453/1122122360854675566/image.png">

## /library reload <library_id>

Reloads the library with the specified id 
(file name without an extension)

<img src="https://media.discordapp.net/attachments/945691411435622453/1122123201741324288/image.png">

# WARNING

Even though the plugin allows you to reload the library. 
I highly recommend not doing this for the reason that the 
old classes remain loaded in the cache, which can lead to
some conflicts between old and new class