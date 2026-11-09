import math
import numpy as np

def m_id():
    return np.eye(4)


def m_trans(x, y, z):
    m = np.eye(4)
    m[0, 3], m[1, 3], m[2, 3] = x, y, z
    return m


def m_rx(a):
    c, s = math.cos(a), math.sin(a)
    m = np.eye(4)
    m[1, 1], m[1, 2], m[2, 1], m[2, 2] = c, -s, s, c
    return m


def m_ry(a):
    c, s = math.cos(a), math.sin(a)
    m = np.eye(4)
    m[0, 0], m[0, 2], m[2, 0], m[2, 2] = c, s, -s, c
    return m


def m_rz(a):
    c, s = math.cos(a), math.sin(a)
    m = np.eye(4)
    m[0, 0], m[0, 1], m[1, 0], m[1, 1] = c, -s, s, c
    return m


def pose_matrix(pose):
    # matches ModelPart.translateAndRotate -> translate * rotateZYX(z, y, x)
    m = m_trans(pose[0], pose[1], pose[2])
    m = m @ m_rz(pose[5])
    m = m @ m_ry(pose[4])
    m = m @ m_rx(pose[3])
    return m


def apply(m, pts):
    pts = np.asarray(pts, dtype=float)
    h = np.concatenate([pts, np.ones((len(pts), 1))], axis=1)
    return (m @ h.T).T[:, :3]


apply = lambda m, pts: (m @ np.concatenate([np.asarray(pts,float), np.ones((len(pts),1))],axis=1).T).T[:, :3]
