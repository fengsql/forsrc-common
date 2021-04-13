package com.forsrc.common.constant;

import com.forsrc.common.tool.Tool;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Enum {

  public enum LanguageType {
    java_(1, "java"),
    python_(2, "python"),
    c_(3, "c"),
    cplus_(4, "c++"),
    cwell_(5, "c#"),
    scala_(6, "scala"),
    go_(7, "go"),
    javascript_(8, "javascript"),
    typescript_(9, "typescript"),
    php_(10, "php"),
    objectivec_(11, "objectivec"),
    kotlin_(12, "kotlin"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static Map<String, LanguageType> map = new HashMap<>();

    static {
      for (LanguageType item : LanguageType.values()) {
        Tool.putMap(map, item.getName(), item);
      }
    }

    LanguageType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static LanguageType get(int id) {
      for (LanguageType item : LanguageType.values()) {
        if (id == item.getId()) {
          return item;
        }
      }
      return null;
    }

    public static LanguageType get(String name) {
      return Tool.getValue(map, name);
    }
  }

  public enum ApplicationType {
    springboot_(1, "springboot"),
    maven_(2, "maven"),
    socket_(3, "socket"),
    bigdata_(4, "bigdata"),
    app_(5, "app"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static Map<String, ApplicationType> map = new HashMap<>();

    static {
      for (ApplicationType item : ApplicationType.values()) {
        Tool.putMap(map, item.getName(), item);
      }
    }

    ApplicationType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static ApplicationType get(int id) {
      for (ApplicationType item : ApplicationType.values()) {
        if (id == item.getId()) {
          return item;
        }
      }
      return null;
    }

    public static ApplicationType get(String name) {
      return Tool.getValue(map, name);
    }
  }

  public enum FrameworkType {
    //springboot
    spring_(1, "spring"),
    //socket
    nettyServer_(2, "netty-server"),
    nettyClient_(3, "netty-client"),
    //bigdata
    flink_(4, "flink"),
    //app
    android_(5, "android"),
    ios_(7, "ios"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static Map<String, FrameworkType> map = new HashMap<>();

    static {
      for (FrameworkType item : FrameworkType.values()) {
        Tool.putMap(map, item.getName(), item);
      }
    }

    FrameworkType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static FrameworkType get(int id) {
      for (FrameworkType item : FrameworkType.values()) {
        if (id == item.getId()) {
          return item;
        }
      }
      return null;
    }

    public static FrameworkType get(String name) {
      return Tool.getValue(map, name);
    }
  }

  public enum DatabaseType {
    mysql_(1, "mysql"),
    oracle_(2, "oracle"),
    sqlserver_(3, "sqlserver"),
    postgresql_(4, "postgresql"),
    greenplum_(5, "greenplum"),
    db2_(6, "db2"),
    memcached_(7, "memcached"),
    redis_(8, "redis"),
    impala_(9, "impala"),
    mongodb_(10, "mongodb"),
    hbase_(11, "hbase"),
    hive_(12, "hive"),
    access_(13, "access"),
    sqlite_(14, "sqlite"),
    h2_(15, "h2"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static Map<String, DatabaseType> map = new HashMap<>();

    static {
      for (DatabaseType item : DatabaseType.values()) {
        Tool.putMap(map, item.getName(), item);
      }
    }

    DatabaseType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static DatabaseType get(int id) {
      for (DatabaseType item : DatabaseType.values()) {
        if (id == item.getId()) {
          return item;
        }
      }
      return null;
    }

    public static DatabaseType get(String name) {
      return Tool.getValue(map, name);
    }
  }

  public enum DataTypeDb {
    bigint_(1, "bigint"),
    binary_(2, "binary"),
    bit_(3, "bit"),
    blob_(4, "blob"),
    char_(5, "char"),
    date_(6, "date"),
    datetime_(7, "datetime"),
    decimal_(8, "decimal"),
    double_(9, "double"),
    enum_(10, "enum"),
    float_(11, "float"),
    geometry_(12, "geometry"),
    geometrycollection_(13, "geometrycollection"),
    int_(14, "int"),
    integer_(15, "integer"),
    json_(16, "json"),
    linestring_(17, "linestring"),
    longblob_(18, "longblob"),
    longtext_(19, "longtext"),
    mediumblob_(20, "mediumblob"),
    mediumint_(21, "mediumint"),
    mediumtext_(22, "mediumtext"),
    multilinestring_(23, "multilinestring"),
    multipoint_(24, "multipoint"),
    multipolygon_(25, "multipolygon"),
    numeric_(26, "numeric"),
    point_(27, "point"),
    polygon_(28, "polygon"),
    real_(29, "real"),
    set_(30, "set"),
    smallint_(31, "smallint"),
    text_(32, "text"),
    time_(33, "time"),
    timestamp_(34, "timestamp"),
    tinyblob_(35, "tinyblob"),
    tinyint_(36, "tinyint"),
    tinytext_(37, "tinytext"),
    varbinary_(38, "varbinary"),
    varchar_(39, "varchar"),
    nvarchar_(40, "nvarchar"),
    year_(41, "year"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static Map<String, DataTypeDb> map = new HashMap<>();

    static {
      for (DataTypeDb item : DataTypeDb.values()) {
        Tool.putMap(map, item.getName(), item);
      }
    }

    DataTypeDb(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static DataTypeDb get(int id) {
      for (DataTypeDb item : DataTypeDb.values()) {
        if (id == item.getId()) {
          return item;
        }
      }
      return null;
    }

    public static DataTypeDb get(String name) {
      return Tool.getValue(map, name);
    }
  }

  public enum FieldPropertyType {
    title_(1, "title"),
    name_(2, "name"),
    type_(3, "type"),
    notnull_(4, "notnull"),
    defaultValue_(5, "defaultValue"),
    description_(6, "description"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static Map<String, FieldPropertyType> map = new HashMap<>();

    static {
      for (FieldPropertyType item : FieldPropertyType.values()) {
        Tool.putMap(map, item.getName(), item);
      }
    }

    FieldPropertyType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static FieldPropertyType get(int id) {
      for (FieldPropertyType item : FieldPropertyType.values()) {
        if (id == item.getId()) {
          return item;
        }
      }
      return null;
    }

    public static FieldPropertyType get(String name) {
      return Tool.getValue(map, name);
    }
  }

  public enum ConstraintType {
    primary_(1, "primary"),
    foreign_(2, "foreign"),
    unique_(3, "unique"),
    check_(4, "check"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static Map<String, ConstraintType> map = new HashMap<>();

    static {
      for (ConstraintType item : ConstraintType.values()) {
        Tool.putMap(map, item.getName(), item);
      }
    }

    ConstraintType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static ConstraintType get(int id) {
      for (ConstraintType item : ConstraintType.values()) {
        if (id == item.getId()) {
          return item;
        }
      }
      return null;
    }

    public static ConstraintType get(String name) {
      return Tool.getValue(map, name);
    }
  }

  public enum ExportFieldType {
    constant_(1, "constant"),
    integer_(2, "integer"),
    long_(3, "long"),
    decimal_(4, "decimal"),
    datetime_(5, "datetime"),
    string_(6, "string"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static final Map<String, ExportFieldType> mapName = new HashMap<>();
    @Getter
    private static final Map<Integer, ExportFieldType> mapValue = new HashMap<>();

    static {
      for (ExportFieldType item : ExportFieldType.values()) {
        mapName.put(item.getName(), item);
        mapValue.put(item.getId(), item);
      }
    }

    ExportFieldType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static ExportFieldType get(int id) {
      return mapValue.get(id);
    }

    public static ExportFieldType get(String name) {
      return Tool.getValue(mapName, name);
    }
  }

  public enum GeneratorItemType {
    src_(1, "src"),
    web_(2, "web"),
    sql_(3, "sql"),
    extend_(4, "extend"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static Map<String, GeneratorItemType> map = new HashMap<>();

    static {
      for (GeneratorItemType item : GeneratorItemType.values()) {
        Tool.putMap(map, item.getName(), item);
      }
    }

    GeneratorItemType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static GeneratorItemType get(int id) {
      for (GeneratorItemType item : GeneratorItemType.values()) {
        if (id == item.getId()) {
          return item;
        }
      }
      return null;
    }

    public static GeneratorItemType get(String name) {
      return Tool.getValue(map, name);
    }
  }

  public enum MysqlKeyType {
    accessible_(1, "accessible"),
    add_(2, "add"),
    all_(3, "all"),
    alter_(4, "alter"),
    analyze_(5, "analyze"),
    and_(6, "and"),
    as_(7, "as"),
    asc_(8, "asc"),
    asensitive_(9, "asensitive"),
    before_(10, "before"),
    between_(11, "between"),
    bigint_(12, "bigint"),
    binary_(13, "binary"),
    blob_(14, "blob"),
    both_(15, "both"),
    by_(16, "by"),
    call_(17, "call"),
    cascade_(18, "cascade"),
    case_(19, "case"),
    change_(20, "change"),
    char_(21, "char"),
    character_(22, "character"),
    check_(23, "check"),
    collate_(24, "collate"),
    column_(25, "column"),
    condition_(26, "condition"),
    constraint_(27, "constraint"),
    continue_(28, "continue"),
    convert_(29, "convert"),
    create_(30, "create"),
    cross_(31, "cross"),
    current_date_(32, "current_date"),
    current_time_(33, "current_time"),
    current_timestamp_(34, "current_timestamp"),
    current_user_(35, "current_user"),
    cursor_(36, "cursor"),
    database_(37, "database"),
    databases_(38, "databases"),
    day_hour_(39, "day_hour"),
    day_microsecond_(40, "day_microsecond"),
    day_minute_(41, "day_minute"),
    day_second_(42, "day_second"),
    dec_(43, "dec"),
    decimal_(44, "decimal"),
    declare_(45, "declare"),
    default_(46, "default"),
    delayed_(47, "delayed"),
    delete_(48, "delete"),
    desc_(49, "desc"),
    describe_(50, "describe"),
    deterministic_(51, "deterministic"),
    distinct_(52, "distinct"),
    distinctrow_(53, "distinctrow"),
    div_(54, "div"),
    double_(55, "double"),
    drop_(56, "drop"),
    dual_(57, "dual"),
    each_(58, "each"),
    else_(59, "else"),
    elseif_(60, "elseif"),
    enclosed_(61, "enclosed"),
    escaped_(62, "escaped"),
    exists_(63, "exists"),
    exit_(64, "exit"),
    explain_(65, "explain"),
    false_(66, "false"),
    fetch_(67, "fetch"),
    float_(68, "float"),
    float4_(69, "float4"),
    float8_(70, "float8"),
    for_(71, "for"),
    force_(72, "force"),
    foreign_(73, "foreign"),
    from_(74, "from"),
    fulltext_(75, "fulltext"),
    get_(76, "get"),
    grant_(77, "grant"),
    group_(78, "group"),
    having_(79, "having"),
    high_priority_(80, "high_priority"),
    hour_microsecond_(81, "hour_microsecond"),
    hour_minute_(82, "hour_minute"),
    hour_second_(83, "hour_second"),
    if_(84, "if"),
    ignore_(85, "ignore"),
    in_(86, "in"),
    index_(87, "index"),
    infile_(88, "infile"),
    inner_(89, "inner"),
    inout_(90, "inout"),
    insensitive_(91, "insensitive"),
    insert_(92, "insert"),
    int_(93, "int"),
    int1_(94, "int1"),
    int2_(95, "int2"),
    int3_(96, "int3"),
    int4_(97, "int4"),
    int8_(98, "int8"),
    integer_(99, "integer"),
    interval_(100, "interval"),
    into_(101, "into"),
    io_after_gtids_(102, "io_after_gtids"),
    io_before_gtids_(103, "io_before_gtids"),
    is_(104, "is"),
    iterate_(105, "iterate"),
    join_(106, "join"),
    key_(107, "key"),
    keys_(108, "keys"),
    kill_(109, "kill"),
    leading_(110, "leading"),
    leave_(111, "leave"),
    left_(112, "left"),
    like_(113, "like"),
    limit_(114, "limit"),
    linear_(115, "linear"),
    lines_(116, "lines"),
    load_(117, "load"),
    localtime_(118, "localtime"),
    localtimestamp_(119, "localtimestamp"),
    lock_(120, "lock"),
    long_(121, "long"),
    longblob_(122, "longblob"),
    longtext_(123, "longtext"),
    loop_(124, "loop"),
    low_priority_(125, "low_priority"),
    master_bind_(126, "master_bind"),
    master_ssl_verify_server_cert_(127, "master_ssl_verify_server_cert"),
    match_(128, "match"),
    maxvalue_(129, "maxvalue"),
    mediumblob_(130, "mediumblob"),
    mediumint_(131, "mediumint"),
    mediumtext_(132, "mediumtext"),
    middleint_(133, "middleint"),
    minute_microsecond_(134, "minute_microsecond"),
    minute_second_(135, "minute_second"),
    mod_(136, "mod"),
    modifies_(137, "modifies"),
    natural_(138, "natural"),
    not_(139, "not"),
    no_write_to_binlog_(140, "no_write_to_binlog"),
    null_(141, "null"),
    numeric_(142, "numeric"),
    on_(143, "on"),
    optimize_(144, "optimize"),
    option_(145, "option"),
    optionally_(146, "optionally"),
    or_(147, "or"),
    order_(148, "order"),
    out_(149, "out"),
    outer_(150, "outer"),
    outfile_(151, "outfile"),
    partition_(152, "partition"),
    precision_(153, "precision"),
    primary_(154, "primary"),
    procedure_(155, "procedure"),
    purge_(156, "purge"),
    range_(157, "range"),
    read_(158, "read"),
    reads_(159, "reads"),
    read_write_(160, "read_write"),
    real_(161, "real"),
    references_(162, "references"),
    regexp_(163, "regexp"),
    release_(164, "release"),
    rename_(165, "rename"),
    repeat_(166, "repeat"),
    replace_(167, "replace"),
    require_(168, "require"),
    resignal_(169, "resignal"),
    restrict_(170, "restrict"),
    return_(171, "return"),
    revoke_(172, "revoke"),
    right_(173, "right"),
    rlike_(174, "rlike"),
    schema_(175, "schema"),
    schemas_(176, "schemas"),
    second_microsecond_(177, "second_microsecond"),
    select_(178, "select"),
    sensitive_(179, "sensitive"),
    separator_(180, "separator"),
    set_(181, "set"),
    show_(182, "show"),
    signal_(183, "signal"),
    smallint_(184, "smallint"),
    spatial_(185, "spatial"),
    specific_(186, "specific"),
    sql_(187, "sql"),
    sqlexception_(188, "sqlexception"),
    sqlstate_(189, "sqlstate"),
    sqlwarning_(190, "sqlwarning"),
    sql_big_result_(191, "sql_big_result"),
    sql_calc_found_rows_(192, "sql_calc_found_rows"),
    sql_small_result_(193, "sql_small_result"),
    ssl_(194, "ssl"),
    starting_(195, "starting"),
    straight_join_(196, "straight_join"),
    table_(197, "table"),
    terminated_(198, "terminated"),
    then_(199, "then"),
    tinyblob_(200, "tinyblob"),
    tinyint_(201, "tinyint"),
    tinytext_(202, "tinytext"),
    to_(203, "to"),
    trailing_(204, "trailing"),
    trigger_(205, "trigger"),
    true_(206, "true"),
    undo_(207, "undo"),
    union_(208, "union"),
    unique_(209, "unique"),
    unlock_(210, "unlock"),
    unsigned_(211, "unsigned"),
    update_(212, "update"),
    usage_(213, "usage"),
    use_(214, "use"),
    using_(215, "using"),
    utc_date_(216, "utc_date"),
    utc_time_(217, "utc_time"),
    utc_timestamp_(218, "utc_timestamp"),
    values_(219, "values"),
    varbinary_(220, "varbinary"),
    varchar_(221, "varchar"),
    varcharacter_(222, "varcharacter"),
    varying_(223, "varying"),
    when_(224, "when"),
    where_(225, "where"),
    while_(226, "while"),
    with_(227, "with"),
    write_(228, "write"),
    xor_(229, "xor"),
    year_month_(230, "year_month"),
    zerofill_(231, "zerofill"),
    ;

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private static Map<String, MysqlKeyType> map = new HashMap<>();

    static {
      for (MysqlKeyType item : MysqlKeyType.values()) {
        Tool.putMap(map, item.getName(), item);
      }
    }

    MysqlKeyType(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public static MysqlKeyType get(int id) {
      for (MysqlKeyType item : MysqlKeyType.values()) {
        if (id == item.getId()) {
          return item;
        }
      }
      return null;
    }

    public static MysqlKeyType get(String name) {
      return Tool.getValue(map, name);
    }
  }

}