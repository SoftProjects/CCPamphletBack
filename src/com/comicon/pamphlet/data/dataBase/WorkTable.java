package com.comicon.pamphlet.data.dataBase;

import com.common.database.DbTable;

public class WorkTable extends DbTable {

	protected WorkTable() {
		super("worktable");
		addColumns("wid", collumsType.INTEGER);
		addColumns("cid", collumsType.INTEGER);
		addColumns("name", collumsType.TEXT);
		addColumns("mode", collumsType.TEXT);
		addColumns("category", collumsType.TEXT);
		addColumns("theme", collumsType.TEXT);
		addColumns("price", collumsType.TEXT);
		addColumns("sample", collumsType.TEXT);
	}
}
