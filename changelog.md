Major rewrite and cleanup
---

- Changed storage to use JSON instead of SQLite 
- Added a config with options to toggle layers and change options like max area size
  - The config can be reloaded alongside Pl3xMap using the `/pl3xmap reload` command
- Added feedback in the form of action messages and sounds when placing/removing markers or points