### 🐴 Private Horses — keep your mount yours!

This mod ensures that **only the owner** can interact with their **horse**, **donkey**, or **mule** — no more random riders jumping on your beloved steed!  
Exceptions apply only to **server operators** and players with the `private-horses.interact` permission.

---

### ⚙️ New Gamerule: `private-horses.disable_damage`

Want extra protection for your tamed animals?  
Enable the new gamerule `private-horses.disable_damage` to prevent any damage to your horse, donkey, or mule — except for **fall damage** and damage caused by the **owner**.  
By default, this rule is **disabled**.

---

### 🔐 Public or private? You decide.

You can freely switch your mount’s accessibility:

- **Make it public** – *Sneak and right-click with* **sugar**
- **Make it private again** – *Sneak and right-click with* **wheat**

This lets you easily share or reclaim access to your horse, donkey, or mule.

---

### 🔄 Transfer ownership

Inspired by the [Transferable Pets](https://modrinth.com/plugin/transferable-pets) mod, this mod also lets you **give your animal to another player**.

To do so:
1. Put the animal on a **lead** (leash).
2. **Sneak and right-click** the player you want to transfer it to.

Done! They’re now the proud new owner.

---

Default config values
```yaml
# Message translations
message:
  # 'AnimalName' is owned by 'PlayerName'
  owned_by: '%s is owned by %s'
  
  # 'PlayerName' is new owner of 'AnimalName'
  new_owner: '%s is new owner of %s'
  
  # 'PlayerName' transferred 'AnimalName' to you
  transfer: '%s transferred %s to you'

# Use the above translations instead of a polymer translation
ignore_polymer: false
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