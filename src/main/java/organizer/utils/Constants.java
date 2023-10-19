package organizer.utils;

import java.util.Arrays;
import java.util.List;

import static organizer.utils.ListUtils.getLeasingCategories;

public class Constants {
	public static final String PATTERN_FOR_SHORT_DATE = "dd.MM.yyyy";
	public static final String PATTERN_FOR_NUMBERS = "#,##0.00";
	public static final String PATTERN_FOR_PERCENT = "0.0#%";
	public static final String STRING_FORMAT_FOR_LOGGING_ERRORS = "Строка excel №%s не загружена!%n" +
																  "Не найдена сл. номенклатура:%n" +
																  "номер заявки - %s, позиция - %s, код КСМ - %s, наименование МТР - %s, сегмент запаса - %s, ЕИ - %s, кол-во - %s, цена(руб. без НДС) - %s, дата поставки - %s, требуется ШМР/ПНР - %s, лизинг - %s, примечение - %s.%n"
																  + "%n";
	public static final String STRING_FORMAT_FOR_LOGGING_ERRORS_ORDERING_RESULT = "Строка excel №%s не загружена!%n" +
																				  "Не найдена сл. заявка:%n" +
																				  "номер заявки - %s, позиция - %s, " + "%n";
	public static final String STRING_FORMAT_FOR_NUMBER_FORMAT_EXCEPTION = "Неверный формат числа %s";
	public static final String STRING_FORMAT_FOR_REMOVE_UNIT = "Единица измерения с ID %s удалена.";
	public static final String STRING_FORMAT_FOR_UNFOUNDED_UNIT = "Единица измерения с ID %s не найдена.";


	public static final String ALL_CATEGORIES = "Все категории";

	public static List<String> LEASING_CATEGORIES = getLeasingCategories();

	public static final String JANUARY = "Январь";
	public static final String FEBRUARY = "Февраль";
	public static final String MARCH = "Март";
	public static final String APRIL = "Апрель";
	public static final String MAY = "Май";
	public static final String JUNE = "Июнь";
	public static final String JULY = "Июль";
	public static final String AUGUST = "Август";
	public static final String SEPTEMBER = "Сентябрь";
	public static final String OCTOBER = "Октябрь";
	public static final String NOVEMBER = "Ноябрь";
	public static final String DECEMBER = "Декабрь";

	public static final List<String> PERIOD = Arrays.asList("Весь год", JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER);

	public static final String FIRST_YEAR = "Первый год";
	public static final String SECOND_YEAR = "Второй год";
	public static final String THIRD_YEAR = "Третий год";
	public static final String FOURTH_YEAR = "Четвертый год";
	public static final String FIFTH_YEAR = "Пятый год";

	public static final List<String> YEARS_FOR_COMPARISON = Arrays.asList(FIRST_YEAR, SECOND_YEAR, THIRD_YEAR, FOURTH_YEAR, FIFTH_YEAR);

	public static final String ELEMENTS_NOT_FOUND = "Нет элементов";
	public static final String INFORMATION_NOT_FOUND = "Нет информации";
	public static final String CLOSE_TITLE = "ЗАКРЫТЬ";

	public static final String WITH_TAX = "с НДС";
	public static final String WITHOUT_TAX = "без НДС";


	public static final String YES = "Да";
	public static final String NO = "Нет";
	public static final String UNDEFINED_EQUIPMENT_GROUP = "Неопределенная группа оборудования";
	public static final String ONLY_CREATED_ORDERINGS = "Только созданные заявки";
	public static final String ONLY_UNCREATED_ORDERINGS = "Только не созданные заявки";
	public static final String ALL_ORDERINGS = "Все заявки";

	public static final String COMPLETED_SUCCESSFULLY = "Выполнено успешно.";
	public static final String DOWNLOAD_OPERATION_COMPLETED = "Операция загрузки выполнена";
	public static final String DATA_DOWNLOAD_COMPLETED_SUCCESSFULLY = "Загрузка данных выполнена успешно";
	public static final String DOWNLOAD_OPERATION_ABORTED = "Операция загрузки прервана!";
	public static final String FILE_FOR_DOWNLOAD_NOT_SELECTED = "Не был выбран файл для загрузки.";
	public static final String CHOOSE_PLACE_FOR_SAVE_FILE = "Выберите место сохранения файла";
	public static final String UNLOADING_OPERATION_ABORTED = "Операция выгрузки прервана!";
	public static final String CHOOSE_FOLDER_FOR_SAVE_FILE_NOT_SELECTED = "Не была выбрана папка для сохранения результатов выгрузки.";
	public static final String BACKUP_ABORTED = "Операция резервирования прервана!";
	public static final String CHOOSE_FOLDER_FOR_BACKUP_NOT_SELECTED = "Не была выбрана папка для сохранения резервной копии.";
	public static final String CHOOSE_PLACE_FOR_SAVE_TEMPLATES = "Выберите место сохранения шаблонов загрузки";
	public static final String ERROR = "Ошибка!!!";
	public static final String ERROR_UPLOADING_FILE = "Ошибка при загрузке файла!";
	public static final String ERROR_UPLOADING_FILE_MESSAGE = "При загрузке файла было выявлено ошибок: ";
	public static final String WRONG_FORMAT = "Неверный формат!!!";
	public static final String NO_CHOOSE_VERSION_BP_FOR_CURRENT_YEAR = "Не выбрана версия БП для текущего года";
	public static final String REMOVE_VERSION_BP = "Удаление версии БП выполнено успешно.";
	public static final String REMOVE_CATEGORY = "Удаление категории выполнено успешно.";
	public static final String REMOVE_CODE_KSM = "Удаление кода КСМ выполнено успешно.";
	public static final String REMOVE_NOMENCLATURE = "Удаление номенклатуры выполнено успешно.";
	public static final String REMOVE_ORDERING = "Удаление заказа выполнено успешно.";
	public static final String REMOVE_ORDERING_RESULT = "Удаление результата заказа на поставку выполнено успешно.";
	public static final String REMOVE_SUBCATEGORY = "Удаление подкатегории выполнено успешно.";
	public static final String REMOVE_TECHNICAL_REQUIREMENT = "Удаление карточки ОЛ/ТТ выполнено успешно.";
	public static final String FORMATION_TEMPLATES_COMPLETED_SUCCESSFULLY = "Формирование шаблонов загрузки выполнено успешно.";
	public static final String ADD_OR_EDIT_BP_COMPLETED_SUCCESSFULLY = "Создание/изменение версии БП выполнено успешно.";
	public static final String ADD_OR_EDIT_CATEGORY_COMPLETED_SUCCESSFULLY = "Создание/изменение категории выполнено успешно.";
	public static final String ADD_OR_EDIT_CODE_KSM_COMPLETED_SUCCESSFULLY = "Создание/изменение кода КСМ выполнено успешно.";
	public static final String ADD_OR_EDIT_NOMENCLATURE_COMPLETED_SUCCESSFULLY = "Создание/изменение номенклатуры выполнено успешно.";
	public static final String ADD_OR_EDIT_ORDERING_COMPLETED_SUCCESSFULLY = "Создание/изменение заявки выполнено успешно.";
	public static final String ADD_OR_EDIT_SUBCATEGORY_COMPLETED_SUCCESSFULLY = "Создание/изменение подкатегории выполнено успешно.";
	public static final String ADD_OR_EDIT_TECHNICAL_REQUIREMENT_COMPLETED_SUCCESSFULLY = "Создание/изменение карточки ОЛ/ТТ выполнено успешно.";
	public static final String SETTINGS_SAVE_SUCCESSFULLY = "Настройки успешно сохранены.";
	public static final String CHANGES_SAVE_SUCCESSFULLY = "Изменения успешно сохранены.";
	public static final String CHANGES_SAVE_ERROR = "Произошла ошибка! Изменения не сохранены.";
	public static final String LOGIN_TITLE = "Вход в приложение";

	public static final String ADD_ID = "Пожалуйста, введите ID.";
	public static final String ADD_FIRST_YEAR_FOR_BP = "Пожалуйста, введите первый год версии БП.";
	public static final String ADD_CATEGORY_NAME = "Пожалуйста, введите имя категории.";
	public static final String ADD_CODE_KSM = "Пожалуйста, введите код КСМ.";
	public static final String ADD_SHORT_NAME_EQUIPMENT = "Пожалуйста, введите краткое наименование МТР.";
	public static final String ADD_YEAR = "Пожалуйста, введите год.";
	public static final String CHOOSE_VERSION_BP = "Пожалуйста, выберите версию БП.";
	public static final String CHOOSE_YEAR_VERSION_BP_FOR_COUNT_TABLE = "Пожалуйста, выберите год версии БП для таблицы кол-ва.";
	public static final String CHOOSE_YEAR_VERSION_BP_FOR_COST_TABLE = "Пожалуйста, выберите год версии БП для таблицы ст-ти.";
	public static final String ADD_LOGIN = "Пожалуйста, введите имя пользователя";
	public static final String ADD_PASSWORD = "Пожалуйста, введите пароль";
	public static final String CHECK_LOGIN = "Пожалуйста, проверьте Ваше имя пользователя.";
	public static final String CHECK_PASSWORD = "Пожалуйста, проверьте Ваш пароль.";
	public static final String ADD_NOMENCLATURE_NAME = "Пожалуйста, введите имя объекта заявки.";
	public static final String CHECK_CODE_KSM_OR_TECHNICAL_REQUIREMENT = "Проверьте правильность ввода кода КСМ и/или номера карточки ОЛ/ТТ.";
	public static final String ADD_SUBCATEGORY_NAME = "Пожалуйста, введите имя подкатегории.";
	public static final String ADD_TECHNICAL_REQUIREMENT_NUMBER = "Пожалуйста, введите номер карточки ОЛ/ТТ.";
	public static final String ADD_TECHNICAL_REQUIREMENT_NAME = "Пожалуйста, введите наименование МТР в карточке ОЛ/ТТ.";
	public static final String ADD_TECHNICAL_REQUIREMENT_FILE_NAME = "Пожалуйста, введите название файла ТЗ.";
	public static final String ADD_SYSTEM_TRANSACTION_NUMBER = "Пожалуйста, введите номер системной транзакции.";
	public static final String ADD_DATE_CREATE_TECHNICAL_REQUIREMENT = "Пожалуйста, введите дату создания карточки ОЛ/ТТ.";
	public static final String ADD_UNIT_NAME = "Пожалуйста, введите наименование единицы измерения.";
	public static final String ADD_NEW_VERSION_BP = "Добавить новую версию бизнес-плана";
	public static final String EDIT_VERSION_BP = "Редактировать версию бизнес-плана";
	public static final String ADD_NEW_CATEGORY = "Добавить новую категорию";
	public static final String EDIT_CATEGORY = "Редактировать категорию";
	public static final String ADD_NEW_CODE_KSM = "Добавление нового кода КСМ";
	public static final String EDIT_CODE_KSM = "Редактирования кода КСМ";
	public static final String ADD_NEW_NOMENCLATURE = "Добавление новой номенклатуры";
	public static final String EDIT_NOMENCLATURE = "Редактирование текущей номенклатуры";
	public static final String CURRENT_CODE_KSM_NOT_FOUND = "Указанный Код КСМ №%s не найден.";
	public static final String CURRENT_TECHNICAL_REQUIREMENT_NOT_FOUND = "Указанная карточка ОЛ/ТТ №%s не найдена.";
	public static final String INCORRECT_CODE_KSM_OR_TECHNICAL_REQUIREMENT = "\nПроверьте правильность ввода кода КСМ и номера карточки ОЛ/ТТ.";
	public static final String CURRENT_TECHNICAL_REQUIREMENT_ALREADY_USED = "Указанный ОЛ/ТТ №%s уже используется с другим КСМ.";
	public static final String ADD_NEW_ORDERING = "Добавить новую заявку";
	public static final String EDIT_ORDERING = "Редактировать текущую заявку";
	public static final String EMPTY_FIELDS = "Проверьте, все ли обязательные поля (*) заполнены.";
	public static final String ADD_NEW_SUBCATEGORY = "Добавление новой подкатегории";
	public static final String EDIT_SUBCATEGORY = "Редактирования текущей подкатегории";
	public static final String ADD_NEW_TECHNICAL_REQUIREMENT = "Добавить новую карточку ОЛ/ТТ";
	public static final String EDIT_TECHNICAL_REQUIREMENT = "Редактировать текущую карточку ОЛ/ТТ";
	public static final String EXIT_DIALOG_TITLE = "Подтверждение";
	public static final String EXIT_DIALOG_MESSAGE = "Вы действительно хотите выйти из программы?";
	public static final String APPLICATION_TITLE = "Органайзер ОНСС";

	public static final String NAME_SHEET_FOR_FILE_TEMPLATE = "Свод";
	public static final String NAME_SHEET_FOR_FILE_TEMPLATE_FOR_SAP = "Лист1";
	public static final String NAME_SHEET_PLANNED_COST_FOR_FILE_TEMPLATE_FOR_SAP = "Обоснование плановой стоимости";
	public static final String PREFIX_FOR_SAVE_FILE_TEMPLATE = "\\Передача потребности на ";
	public static final String FILENAME_FOR_UNLOAD_CATEGORIES = "\\Категории";
	public static final String FILENAME_FOR_UNLOAD_CODES_KSM = "\\Коды КСМ";
	public static final String FILENAME_FOR_UNLOAD_SUBCATEGORIES = "\\Подкатегории";
	public static final String FILENAME_FOR_UNLOAD_TECHNICAL_REQUIREMENTS = "\\ОЛ_ТТ";
	public static final String FILENAME_FOR_UNLOAD_NOMENCLATURES = "\\Номенклатура";
	public static final String FILENAME_FOR_UNLOAD_BUSINESS_PLANS = "\\Бизнес-план";
	public static final String FILENAME_FOR_UNLOAD_ORDERINGS = "\\Заявки ОНВСС на ";
	public static final String FILENAME_FOR_ERROR_LOG = "errorLog\\Ошибки загрузки от ";
	public static final String FILENAME_FOR_ORDERING_RESULT_ERROR_LOG = "errorLog\\Ошибки загрузки результатов от ";
	public static final String FILENAME_FOR_TEMPLATE_SAP = "\\Шаблон загрузки на ";
	public static final String FILENAME_SUFFIX_YEAR_FOR_TEMPLATE_SAP = " год от ";
	public static final String FILENAME_SUFFIX_ACCESSORY_FOR_TEMPLATE_SAP = " для ";
	public static final String FILENAME_FOR_UNLOAD_DETAIL_ANALYTICS = "\\Аналитика по заявкам ОНВСС на ";
	public static final String FILENAME_FOR_UNLOAD_SUM_ANALYTICS = "\\Сводная аналитика по заявкам ОНВСС на ";
	public static final String FILENAME_FOR_UNLOAD_ORDERING_RESULTS = "\\Результаты закупок";

	// Константы для формирования шаблона заявки перед загрузкой в SAP
	public static final String TYPE_OF_ORDERING_ID = "ZMTR";
	public static final String TYPE_OF_CONTENTION_ID = "Q";
	public static final String TYPE_OF_CONTENTION_FOR_RELATED_SERVICES_ID = "X";
	public static final String TYPE_OF_POSITION_ID = "U";
	public static final String FACTORY_ID = "N01F";
	public static final String STORAGE_ID = "VIRT";
	public static final String PURCHASING_GROUP_ID = "SN1";
	public static final String PURCHASING_ORGANIZATION_ID = "1322";
	public static final String SUPPLIER_FACTORY_ID = "N01F";
	public static final String RELEASING_STORAGE_ID = "M000";
	public static final String MANAGER_ID = "CFR1322010124000";
	public static final String FINANCIAL_POSITION_ID = "2.9.001";
	public static final String TYPE_OF_ACTIVITY_ID = "03";
	public static final String CUSTOMER_DIVISION_ID = "10048791";
	public static final String SIGN_OF_RELATED_SERVICES = "X";
	public static final String DELIVERY_ADDRESS_ID = "194238";
	public static final String CODE_KSM_INSTALLATION_SUPERVISION = "3023301";
	public static final String SHORT_NAME_CODE_KSM_INSTALLATION_SUPERVISION = "Шеф-монтажные работы НПО";
	public static final String CODE_KSM_COMMISSIONING_WORKS = "3021595";
	public static final String SHORT_NAME_CODE_KSM_COMMISSIONING_WORKS = "НаладочныеПусконаладочныеРаботы на НПО";
	public static final String PLACE_OF_OCCURRENCE_OF_COSTS_ID = "8980000000";
	public static final String TEXT_ID = "ZPLA";

	public static final String FILE_CHOOSER_WINDOW_TITLE_PREFIX = "Выберите файл ";
	public static final String FILE_CHOOSER_WINDOW_TITLE_SUFFIX = " для загрузки в БД";

	public static final String CONTEXT_MENU_COPY_CELL = String.format("%-35s%-15s", "Копировать ячейку", "CTRL+C");
	public static final String CONTEXT_MENU_COPY_ROW = String.format("%-35s%-15s", "Копировать строку", "CTRL+SHIFT+C");
	public static final String DUPLICATE_ROW = String.format("%-35s%-15s", "Дублировать строку", "CTRL+D");
	public static final String REMOVE_ROW = String.format("%-35s%-15s", "Удалить строку", "");

	public static final String CURRENCY_WITH_TAX_SUFFIX = " руб. с НДС";
	public static final String BP_UPLOAD_TITLE_SUFFIX = "бизнес-плана";
	public static final String CATEGORIES_UPLOAD_TITLE_SUFFIX = "категорий";
	public static final String SUBCATEGORIES_UPLOAD_TITLE_SUFFIX = "подкатегорий";
	public static final String CODES_KSM_UPLOAD_TITLE_SUFFIX = "кодов КСМ";
	public static final String ORDERINGS_UPLOAD_TITLE_SUFFIX = "заявок";
	public static final String RESULTS_ORDERINGS_UPLOAD_TITLE_SUFFIX = "результатов заявок";
	public static final String NOMENCLATURES_UPLOAD_TITLE_SUFFIX = "номенклатуры";
	public static final String TECHNICAL_REQUIREMENTS_UPLOAD_TITLE_SUFFIX = "ОЛ/ТТ";
}
