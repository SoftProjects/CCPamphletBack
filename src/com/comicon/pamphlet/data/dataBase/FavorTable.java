package com.comicon.pamphlet.data.dataBase;

import com.common.database.DbTable;

public class FavorTable extends DbTable {

	protected FavorTable() {
		super("favortable");
		addColumns("cid", collumsType.INTEGER);
		addColumns("isfavour", collumsType.TEXT);
	}
}
