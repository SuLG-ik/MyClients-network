# 🚀 MyClients-network

👷 This project is in active development.

### ✨ Demo

- [Watch](/docs/DEMO.md) demo

## 🏗️️ Built with

| What         | How                                                                      |
|--------------|--------------------------------------------------------------------------|
| 🧠 Framework | [Ktor 2.0](https://github.com/ktorio/ktor)                               |                                                                                          ||
| 💉 DI        | [Koin 3.0](https://github.com/InsertKoinIO/koin)                         |                                                                                                                             |
| 🌊 Async     | [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)       |
| 📄 JSON      | [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) ||
| 🛣️ Auth     | [JWT](https://jwt.io/)                                                   |
| ⌨ DB         | Deploy - Mongo, in process - migrate to postgres                         |

## 🥼 Primary entrypoint

#### Each entrypoint has prefix `/v1` as api version

- /customers
- /cards
- /employees
- /accounts
- /auth
- /companies
- /services
- /sessions

## ✍️ Author

👤 **Sulgik** (Vladimir Nenashkin)

* Telegram: <a href="https://t.me/vollllodya" target="_blank">@vollllodya</a>
* Email: nenashkinvov@gmail.com

## ☑️ TODO

- [ ] Migrate to postgres
- [ ] Add companies and stations separations
- [ ] Add records
- [ ] Add salary calculating
- [ ] Add more statistic
- [ ] Add public api, for example, for sites

## 📝 License

```
Copyright © 2022 - Vladimir Nenashkkin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```