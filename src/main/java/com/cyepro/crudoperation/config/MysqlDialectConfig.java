package com.cyepro.crudoperation.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.MySQLDialect; 
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MysqlDialectConfig extends MySQLDialect { 
    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        functionContributions.getFunctionRegistry().register(
            "group_concat",
            new StandardSQLFunction("group_concat", StandardBasicTypes.STRING)
        );
    }
}