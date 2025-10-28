Major rewrite and cleanup
---

- Changed storage to use JSON instead of SQLite 
- Added a config with options to toggle layers and change options like max area size and layer priority
  - The config can be reloaded alongside Pl3xMap using the `/pl3xmap reload` command
- Added feedback in the form of action messages and sounds when placing/removing markers or points

## !! Warning !!
This storage rewrite is not backwards compatible with previous versions of the plugin.
All existing markers and areas will be lost upon updating to this version.