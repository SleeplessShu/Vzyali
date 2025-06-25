<h1>
  <img src="https://github.com/user-attachments/assets/772767cd-2287-4227-be7f-ed5c17144b09" height="28"/>
  Vzyali - агрегатор вакансий
</h1>

---

Приложение для Android, которое позволяет искать вакансии, фильтровать их по параметрам и добавлять в избранное. Содержит современные практики архитектуры и чистого кода, построено на базе MVVM с разделением по слоям Clean Architecture.

---

## ⚙️ Возможности
- Поиск вакансий по ключевым словам
- Фильтрация результатов по зарплате, локации и опыту
- Экран избранного
- Детальный просмотр вакансии
- Поддержка сохранения фильтров

---

## 🧱 Стек технологий
Kotlin, MVVM, Clean Architecture, Koin, Coroutines, Flow, Retrofit, RecyclerView, ViewBinding, Material Components, Retrofit

---

## 🗂 Архитектура
Проект разделён на слои:
- **UI / Presentation** - `Fragment`, `ViewModel`, `State`
- **Domain** - бизнес-логика и use case'ы
- **Data** - репозитории, DTO, API
## 📦 Сетевое взаимодействие
Вакансии получаются через REST API. Сетевой слой построен с использованием `Retrofit`.

---

## 📁 Примеры экранов
<table>
  <tr>
      <td align="center" valign="top">
      <img src="https://github.com/user-attachments/assets/70a32be7-d0f0-4069-ac2b-e823a5e6b6fb" style="height:300px; object-fit:contain;"/><br/>
      <sub>Поиск</sub>
    </td>
    <td align="center" valign="top">
      <img src="https://github.com/user-attachments/assets/16fcb7ac-3e5e-43df-92a5-028414cf04af" style="height:300px; object-fit:contain;"/><br/>
      <sub>Избранное</sub>
    </td>
    <td align="center" valign="top">
      <img src="https://github.com/user-attachments/assets/b675831d-cbbe-49f2-b8ba-726eb58bfd9d" style="height:300px; object-fit:contain;"/><br/>
      <sub>Фильтры</sub>
    </td>
      <td align="center" valign="top">
      <img src="https://github.com/user-attachments/assets/a2068227-27c7-4639-8cab-67be78f859e4" style="height:300px; object-fit:contain;"/><br/>
      <sub>Детали вакансии</sub>
    </td>
  </tr>
</table>

---

## 🔗 Ссылка на проект
[GitHub: Vzyali](https://github.com/SleeplessShu/Vzyali)
