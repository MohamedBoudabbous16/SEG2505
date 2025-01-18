package com.example.sqldemo;

import android.database.Cursor;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import java.lang.Class;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class EmailDao_Impl implements EmailDao {
  private final RoomDatabase __db;

  public EmailDao_Impl(RoomDatabase __db) {
    this.__db = __db;
  }

  @Override
  public Object getAll(final Continuation<? super List<Email>> $completion) {
    final String _sql = "SELECT * FROM email";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    int _argIndex = 1;
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Object _result;
      if(_cursor.moveToFirst()) {
        _result = new Object();
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
