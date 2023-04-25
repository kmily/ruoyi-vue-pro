import request from '@/utils/request'

// 创建工程日志列表
export function createProjectImplLog(data) {
  return request({
    url: '/oa/project-impl-log/create',
    method: 'post',
    data: data
  })
}

// 更新工程日志列表
export function updateProjectImplLog(data) {
  return request({
    url: '/oa/project-impl-log/update',
    method: 'put',
    data: data
  })
}

// 删除工程日志列表
export function deleteProjectImplLog(id) {
  return request({
    url: '/oa/project-impl-log/delete?id=' + id,
    method: 'delete'
  })
}

// 获得工程日志列表
export function getProjectImplLog(id) {
  return request({
    url: '/oa/project-impl-log/get?id=' + id,
    method: 'get'
  })
}

// 获得工程日志列表分页
export function getProjectImplLogPage(query) {
  return request({
    url: '/oa/project-impl-log/page',
    method: 'get',
    params: query
  })
}

// 导出工程日志列表 Excel
export function exportProjectImplLogExcel(query) {
  return request({
    url: '/oa/project-impl-log/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
