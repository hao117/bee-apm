先拦截`Connection::prepareStatement`获取`SQL`语句
再拦截`PreparedStatement::setXXX`获取参数
接着拦截`PreparedStatement::executeXXX`结束处理
