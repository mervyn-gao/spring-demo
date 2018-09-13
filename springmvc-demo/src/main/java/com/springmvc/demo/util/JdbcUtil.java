package com.springmvc.demo.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuli
 * @create 2018/8/1
 */
public class JdbcUtil {

    private final static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    /**
     * 条件查询(依赖于mybatis的mapper 和 Example) 查到返回List 查不到返回null
     *
     * @param requestPo
     * @param targetType
     * @return
     */
    public static <T, R> List<T> findByConditionWithConverter(R requestPo, Class<T> targetType) {
        return findByCondition(BeanUtil.copyProperties(requestPo, targetType));
    }

    /**
     * 条件查询(依赖于mybatis的mapper 和 Example) 查到返回List 查不到返回null
     *
     * @param conditionPo
     * @param <T>
     * @return
     */
    public static <T> List<T> findByCondition(T conditionPo) {
        Object result = operateRecord(conditionPo, "selectByExample", null);
        if (CollectionUtils.isEmpty((List<T>) result)) {
            return null;
        }
        return (List<T>) result;
    }

    /**
     * 条件删除 (依赖于mybatis的mapper 和 Example) 返回删除条数
     *
     * @param requestPo
     * @param <T>
     * @return
     */
    public static <T, R> int deleteByConditionWithConverter(R requestPo, Class<T> targetType) {
        return deleteByCondition(BeanUtil.copyProperties(requestPo, targetType));
    }

    /**
     * 条件删除 (依赖于mybatis的mapper 和 Example) 返回删除条数
     *
     * @param conditionPo
     * @param <T>
     * @return
     */
    public static <T> int deleteByCondition(T conditionPo) {
        Object result = operateRecord(conditionPo, "deleteByExample", null);
        if (result == null) {
            return 0;
        }
        return (int) result;
    }

    /**
     * 条件更新 (依赖于mybatis的mapper 和 Example) 返回更新条数
     *
     * @param requestPo(条件),T dataPo(更新成的数据)
     * @param <T>
     * @return
     */
    public static <T, R> int updateByConditionWithConverter(T dataPo, R requestPo, Class<T> targetType) {
        return updateByCondition(dataPo, BeanUtil.copyProperties(requestPo, targetType));
    }

    /**
     * 条件更新 (依赖于mybatis的mapper 和 Example) 返回更新条数
     *
     * @param conditionPo(条件),T dataPo(更新成的数据)
     * @param <T>
     * @return
     */
    public static <T> int updateByCondition(T dataPo, T conditionPo) {
        Object result = operateRecord(conditionPo, "updateByExampleSelective", dataPo);
        if (result == null) {
            return 0;
        }
        return (int) result;
    }

    /**
     * 插入一条数据
     *
     * @param requestDataPo
     * @return
     */
    public static <T, R> int insertRecordWithConverter(R requestDataPo, Class<T> targetType) {
        return insertRecord(BeanUtil.copyProperties(requestDataPo, targetType));
    }

    /**
     * 插入一条数据
     *
     * @param dataPo
     * @return
     */
    public static <T> int insertRecord(T dataPo) {
        Object result = executeSql("insertSelective", dataPo);
        if (result == null) {
            return 0;
        }
        return (int) result;
    }

    /**
     * 需和Spring @Transaction配合使用 单独使用无效果
     *
     * @param mapper
     * @param requestPoList
     * @param <T>
     * @return
     */
    public static <T, R> int executeInsertSelectiveBatchWithConverter(Object mapper, List<T> requestPoList, Class<T> targetType) {
        return executeInsertSelectiveBatch(mapper, BeanUtil.copyList(requestPoList, targetType));
    }

    /**
     * 需和Spring @Transaction配合使用 单独使用无效果
     *
     * @param mapper
     * @param poList
     * @param <T>
     * @return
     */
    public static <T> int executeInsertSelectiveBatch(Object mapper, List<T> poList) {
        List<Object[]> paramList = new ArrayList<>(poList.size());
        for (int i = 0; i < poList.size(); i++) {
            Object[] param = new Object[1];
            param[0] = poList.get(i);
            paramList.add(param);
        }
        return executeSqlBatch(mapper, "insertSelective", paramList, 1);
    }

    /**
     * 需和Spring @Transaction配合使用 单独使用无效果
     *
     * @param mapper
     * @param paramList
     * @return
     */
    public static int executeUpdateByConditionBatch(Object mapper, List<Object[]> paramList) {
        try {
            for (int i = 0; i < paramList.size(); i++) {
                Object[] param = paramList.get(i);
                Object conditionPo = param[1];
                String name = conditionPo.getClass().getName();
                String exampleName = name + "Example";
                Class<?> exampleClazz = Class.forName(exampleName);
                Object example = exampleClazz.newInstance();
                Method createCriteriaMethod = exampleClazz.getMethod("createCriteria");
                Object criteria = createCriteriaMethod.invoke(example);
                addCriteriaUseField(criteria, conditionPo);
                param[1] = example;
            }
        } catch (Exception e) {
            throw new RuntimeException("查询条件封装错误", e);
        }
        return executeSqlBatch(mapper, "updateByExampleSelective", paramList, 1000);
    }


    public static <T> Object executeSql(String methodName, T po) {
        try {
            String simpleName = po.getClass().getSimpleName();
            if (simpleName.endsWith("Vo")) {
                simpleName = simpleName.substring(0, simpleName.length() - 2);
            }
            Method m;
            Object mapper;
            try {
                mapper = SpringContextHolder.getBean("biz" + simpleName + "Mapper");
                m = mapper.getClass().getMethod(methodName, po.getClass());
            } catch (Exception e) {
                simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
                mapper = SpringContextHolder.getBean(simpleName + "Mapper");
                m = mapper.getClass().getMethod(methodName, po.getClass());
            }
            return m.invoke(mapper, po);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int executeSqlBatch(Object mapper, String methodName, List<Object[]> paramList, int
            batchCount) {
        if (CollectionUtils.isEmpty(paramList)) {
            return 0;
        }
        SqlSession session = null;
        Method m;
        int size;
        Class<?> mapperClass;
        try {
            mapperClass = getMapperTarget(mapper);
            Class<?>[] classes = null;
            try {
                Object[] param = paramList.get(0);
                if (param.length < 1) {
                    return 0;
                }
                classes = new Class<?>[param.length];
                for (int i = 0; i < param.length; i++) {
                    classes[i] = param[i].getClass();
                }
                m = mapperClass.getMethod(methodName, classes);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder("");
                if (classes != null) {
                    for (Class c : classes) {
                        sb.append(c.toString()).append(" | ");
                    }
                }
                throw new RuntimeException(mapperClass.getName() + "中找不到" + methodName + "方法,入参类型: " + sb);
            }

            SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate) SpringContextHolder.getBean("sqlsessionTemplate");
            // 在spring manager 环境下 这里设置的autocommit true或false无意义 会因有无@Transaction注解决定 by hj
            session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
            try {
                mapper = session.getMapper(mapperClass);
            } catch (Exception e) {
                throw new RuntimeException("session中找不到" + mapperClass.getName() + "对应的Mapper");
            }

            size = paramList.size();
            int result = 0;
            for (int i = 1; i < size + 1; i++) {
                m.invoke(mapper, paramList.get(i - 1));
                if (i % batchCount == 0 || i == size) {
                    //手动每 batchCount 个一提交，提交后无法回滚  可以被@Transaction管理
                    session.commit();
                    //清理缓存，防止溢出
                    session.clearCache();
                    //清除一级缓存
                    sqlSessionTemplate.clearCache();
                }
            }
            // 暂无法获得成功条数
            return result;
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    private static Class getMapperTarget(Object target) {
        try {
            Object t = AopTargetUtils.getTarget(target);
            Field h2 = t.getClass().getSuperclass().getDeclaredField("h");
            h2.setAccessible(true);
            Field mapperInterface = h2.get(t).getClass().getDeclaredField("mapperInterface");
            mapperInterface.setAccessible(true);
            return (Class) mapperInterface.get(h2.get(t));
        } catch (Exception e) {
            logger.error("ToolUtils.getMapperTarget 异常", e);
        }
        return target.getClass();
    }

    private static <T> Object operateRecord(T po, String methodName, Object params) {
        try {
            if (po == null) {
                return null;
            }
            String name = po.getClass().getName();
            String exampleName = name + "Example";
            Class<?> exampleClazz = Class.forName(exampleName);
            Object example = exampleClazz.newInstance();
            Method createCriteriaMethod = exampleClazz.getMethod("createCriteria");
            Object criteria = createCriteriaMethod.invoke(example);
            addCriteriaUseField(criteria, po);
            String simpleName = po.getClass().getSimpleName();
            Method m;
            Object mapper;
            try {
                mapper = SpringContextHolder.getBean("biz" + simpleName + "Mapper");
                if (params == null) {
                    m = mapper.getClass().getMethod(methodName, exampleClazz);
                } else {
                    m = mapper.getClass().getMethod(methodName, params.getClass(), exampleClazz);
                }
            } catch (Exception e) {
                simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
                mapper = SpringContextHolder.getBean(simpleName + "Mapper");
                if (params == null) {
                    m = mapper.getClass().getMethod(methodName, exampleClazz);
                } else {
                    m = mapper.getClass().getMethod(methodName, params.getClass(), exampleClazz);
                }
            }
            if (params == null) {
                return m.invoke(mapper, example);
            }
            return m.invoke(mapper, params, example);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addCriteriaUseField(Object criteria, Object po) {
        Class cls = po.getClass();
        while (!Object.class.getName().equals(cls.getName())) {
            addCriteriaUseCurrentClassField(criteria, po, cls);
            cls = cls.getSuperclass();
        }
    }

    private static void addCriteriaUseCurrentClassField(Object criteria, Object po, Class cls) {
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            try {
                Method m = po.getClass().getMethod("get" + name);
                Object value = m.invoke(po);
                if (value != null && !"".equals(value)) {
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = criteria.getClass().getMethod("and" + name + "EqualTo", parameterTypes);
                    Object[] objects = new Object[1];
                    objects[0] = value;
                    m.invoke(criteria, objects);
                }
            } catch (Exception e) {
                continue;
            }
        }
    }
}
