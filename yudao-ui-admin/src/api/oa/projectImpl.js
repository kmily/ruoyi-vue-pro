import request from '@/utils/request'

// 创建工程实施列
export function createProjectImpl(data) {
  return request({
    url: '/oa/project-impl/create',
    method: 'post',
    data: data
  })
}

// 更新工程实施列
export function updateProjectImpl(data) {
  return request({
    url: '/oa/project-impl/update',
    method: 'put',
    data: data
  })
}

// 删除工程实施列
export function deleteProjectImpl(id) {
  return request({
    url: '/oa/project-impl/delete?id=' + id,
    method: 'delete'
  })
}

// 获得工程实施列
export function getProjectImpl(id) {
  return request({
    url: '/oa/project-impl/get?id=' + id,
    method: 'get'
  })
}

// 获得工程实施列分页
export function getProjectImplPage(query) {
  return request({
    url: '/oa/project-impl/page',
    method: 'get',
    params: query
  })
}

// 导出工程实施列 Excel
export function exportProjectImplExcel(query) {
  return request({
    url: '/oa/project-impl/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
