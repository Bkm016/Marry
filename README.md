# Marry
> Marry in Minecraft

---
This plugin required **[TabooLib](http://www.mcbbs.net/thread-773065-1-1.html)**  
Put "TabooLib.jar" and "Marry.jar" into the "plugins" folder  

---
### Commands 
#### Player
/marry —— See help.  
/marry send [ID] —— Request for Marriage.  
/marry yes [ID] —— Accept for request.  
/marry no [ID] —— Refuse the request.  
/marry info —— Marriage information.  
/marry list —— Marriage information in sevrer.  
/marry divorce —— Divorce.  
#### Console
/marry —— Reload configuration.  

---
### How to create a ring
1. Open the "plugins/TabooLib/items.yml" folder.
2. Append the following code. [Item Format](https://www.showdoc.cc/taboolib?page_id=101814822112191)
```yaml
戒指:
  material: 287
  name: '&e&l[&6&l24K&e&l] &f纯棉戒指'
  lore:
  - ''
  - '&8&o用来骗骗小学生还可以'
  enchants:
    1: 1
  flags:
  - 'HIDE_ENCHANTS'  
```
3. Execute the command **"/taboolib ireload"** and **"/taboolib item 戒指"**

---
### How to create a Love affair
1. Open the "plugins/TabooLib/items.yml" folder.
2. Append the following code. [Item Format](https://www.showdoc.cc/taboolib?page_id=101814822112191)
```yaml
玫瑰:
  material: 38
  data: 4
  name: '&e&l[&6&l24K&e&l] &f纯棉玫瑰'
  lore:
  - ''
  - '&8&o用来骗骗小学生还可以'
  enchants:
    1: 1
  flags:
  - 'HIDE_ENCHANTS'
```
3. Execute the command **"/taboolib ireload"** and **"/taboolib item 玫瑰"**

---
Help the items, can be viewed in **[TabooLib in MCBBS](http://www.mcbbs.net/thread-773065-1-1.html)**
