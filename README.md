# jmsApplication
Task two for junior
Время затраченное на отправку и чтение сообщения сохраняемого на диск (Persistent message) в разных режимах сессий:
SessionMode - AUTO_ACKNOWLEDGE, 1178 ms
SessionMode - CLIENT_ACKNOWLEDGE, 1024 ms
SessionMode - DUPS_OK_ACKNOWLEDGE, 1122 ms
SessionMode - SESSION_TRANSACTED, 1137 ms  
  
  
Время затраченное на отправку и чтение сообщения НЕ сохраняемого на диск (NonPersistent message) в разных режимах сессий:
SessionMode - AUTO_ACKNOWLEDGE, 945 ms
SessionMode - CLIENT_ACKNOWLEDGE, 992 ms
SessionMode - DUPS_OK_ACKNOWLEDGE, 954 ms
SessionMode - SESSION_TRANSACTED, 1055 ms  
  
Выводы:  
Сообщения, сохраняемые на диск, требуют больше времени для отправки и чтения в сравнении с непостоянными сообщениями из-за необходимости гарантировать их сохранность.  
Режим CLIENT_ACKNOWLEDGE часто предоставляет наилучший баланс между производительностью и надежностью, особенно для непостоянных сообщений.  
Режим DUPS_OK_ACKNOWLEDGE также может быть эффективным в определенных случаях, где небольшая потеря сообщений является приемлемой.  
Режим SESSION_TRANSACTED и AUTO_ACKNOWLEDGE обладают большей надежностью, но могут иметь небольшое увеличение времени обработки из-за дополнительных проверок и операций с транзакциями.  
Выбор режима сессии зависит от конкретных требований приложения: если важна скорость и производительность, то CLIENT_ACKNOWLEDGE или DUPS_OK_ACKNOWLEDGE могут быть хорошими вариантами.  
Если требуется полная надежность и транзакционная поддержка, то SESSION_TRANSACTED или AUTO_ACKNOWLEDGE могут быть предпочтительны.  
