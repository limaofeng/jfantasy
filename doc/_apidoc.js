// ------------------------------------------------------------------------------------------
// General apiDoc documentation blocks and old history blocks.
// ------------------------------------------------------------------------------------------

// ------------------------------------------------------------------------------------------
// Current Success.
// ------------------------------------------------------------------------------------------


// ------------------------------------------------------------------------------------------
// Current Errors.
// ------------------------------------------------------------------------------------------
/**
 * @apiDefine GeneralError
 * @apiVersion 3.3.2
 *
 * @apiError NoAccessRight      未授权,不能访问接口,对应 response status <code>401</code>
 * @apiError NotFound           对应的资源不存在,对应 response status <code>404</code>
 *
 * @apiErrorExample  Response (example):
 *     HTTP/1.1 400 Bad Request
 *     {
 *       "error": "UserNameTooShort"
 *     }
 */


// ------------------------------------------------------------------------------------------
// Current Permissions.
// ------------------------------------------------------------------------------------------
/**
 * @apiDefine admin 需要Admin的访问权限.
 * Optionally you can write here further Informations about the permission.
 *
 * An "apiDefinePermission"-block can have an "apiVersion", so you can attach the block to a specific version.
 *
 * @apiVersion 3.3.2
 */


// ------------------------------------------------------------------------------------------
// History.
// ------------------------------------------------------------------------------------------
