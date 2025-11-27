# Fractal Flame Renderer

Рендерер фрактального пламени на Java 22 с CLI-демо: случайно строит аффинные преобразования и вариации, рендерит и сохраняет PNG.

## Структура проекта
- `src/main/java/backend/academy/fractal` — ядро: генерация коэффициентов, вариации, рендерер, CLI `Main`.
- `src/main/resources` — конфиг логирования `log4j2.xml`.
- `src/test/java/backend/academy/samples` — примеры тестов JUnit 5/AssertJ/Mockito/Awaitility/RestAssured.
- Корень: `pom.xml`, `checkstyle.xml`, `pmd.xml`, `spotbugs-excludes.xml`, `lombok.config`, `.editorconfig`.

## Требования
- JDK 22+ и Maven 3.8.8+ (enforcer проверяет); используйте `./mvnw`/`mvnw.cmd`.
- Нужен AWT для записи PNG.

## Сборка и тесты
- Полный цикл: `./mvnw clean verify` (Windows: `mvnw.cmd ...`) — компиляция, юнит/интеграционные тесты, статический анализ, JaCoCo.
- Быстрые тесты: `./mvnw test`.
- Только линтеры: `./mvnw checkstyle:check modernizer:modernizer spotbugs:check pmd:check pmd:cpd-check`.

## Запуск демо
- Запустите: `./mvnw -q exec:java -Dexec.mainClass=backend.academy.fractal.flame.Main`.
- Значения по умолчанию в `Main`: 1920x1080, 10M сэмплов, гамма 2.2, один поток, вывод `fractal_flame.png` в корне.
- Меняйте размер, число сэмплов, `symmetry`, список вариаций в `createTransformations` или формат в `ImageUtils.save` (`PNG`/`JPG`).

