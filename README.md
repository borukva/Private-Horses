### Private Horses

Sick of having your horse, donkey, or mule jacked? <br>
With this mod, you can relax — no one but you can ride or leash your animal now!

---

Only the owner can interact with their horse, donkey, or mule, except for operators and players with the `private-horses.interact` permission.

Additionally, a new gamerule, `private-horses.disable_damage`, has been added. By default, it is disabled, but when enabled, it prevents any damage to tamed horses, donkeys, and mules, except for fall damage and damage from their owner.

Also, I adapted the idea of transferring the animal from [Transferable Pets](https://modrinth.com/plugin/transferable-pets) mod. <br>
It works same way: leash your horse/donkey/mule, press shift and right-click on a player you want transfer to.

If you`re not using Polymer this config will be generated.
```yaml
# Message translations
message:
  # 'AnimalName' is owned by 'PlayerName'
  owned_by: '%s is owned by %s'
  
  # 'PlayerName' is new owner of 'AnimalName'
  new_owner: '%s is new owner of %s'
  
  # 'PlayerName' transferred 'AnimalName' to you
  transfer: '%s transferred %s to you'
```

Note: If a player tries to equip horse armor on someone else's horse, it will appear equipped due to a client-side visual bug. However, the armor will remain in the player's inventory.

---
You can visit my little [contact card](https://somykos.github.io/web/), <br>
And you are welcome to support me via the following links:<br>
<a href="https://ko-fi.com/somyk">
<img src="https://raw.githubusercontent.com/somykOS/web/c03742bd86ca2ce0f6f39bcd3cfe683ad98926a2/public/external/kofi_s_logo_nolabel.svg" alt="ko-fi" width="100"/>
</a>
<a href="https://send.monobank.ua/jar/8RCzun35pC">
<img src="https://raw.githubusercontent.com/somykOS/web/5ac2e685429eb0cc369dc220ce3b93d2a22893c0/public/external/monobank_logo.svg" alt="monobank" width="80"/>
</a>